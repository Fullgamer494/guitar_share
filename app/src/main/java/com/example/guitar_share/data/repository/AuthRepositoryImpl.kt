package com.example.guitar_share.data.repository

import com.example.guitar_share.data.mapper.toDomain
import com.example.guitar_share.data.model.UserDto
import com.example.guitar_share.domain.model.User
import com.example.guitar_share.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl : AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = com.google.firebase.storage.FirebaseStorage.getInstance()

    override fun isUserLoggedIn(): Boolean = auth.currentUser != null

    override fun getCurrentUserId(): String? = auth.currentUser?.uid

    override suspend fun login(email: String, pass: String): Result<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, pass: String, username: String, guitarLevel: String, profilePictureUri: String?): Result<Boolean> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
            val uid = authResult.user?.uid ?: throw Exception("Error al obtener UID")

            var finalProfileUrl = "https://randomuser.me/api/portraits/lego/1.jpg"

            if (profilePictureUri != null) {
                try {
                    val uri = android.net.Uri.parse(profilePictureUri)
                    val storageRef = storage.reference.child("profile_images/$uid.jpg")
                    storageRef.putFile(uri).await()
                    finalProfileUrl = storageRef.downloadUrl.await().toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            val newUserDto = UserDto(
                id = uid,
                username = username,
                email = email,
                guitarLevel = guitarLevel,
                profilePictureUrl = finalProfileUrl
            )
            firestore.collection("users").document(uid).set(newUserDto).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserData(uid: String): Result<User> {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            val userDto = document.toObject(UserDto::class.java)
            if (userDto != null) {
                Result.success(userDto.toDomain())
            } else {
                Result.failure(Exception("Usuario no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(uid: String, username: String, email: String, profilePictureUri: String?): Result<Boolean> {
        return try {
            val updates = mutableMapOf<String, Any>(
                "username" to username,
                "email" to email
            )

            if (profilePictureUri != null) {
                // Check if it's a local URI (needs upload) or already a remote URL
                if (!profilePictureUri.startsWith("http")) {
                    val uri = android.net.Uri.parse(profilePictureUri)
                    val storageRef = storage.reference.child("profile_images/$uid.jpg")
                    storageRef.putFile(uri).await()
                    val downloadUrl = storageRef.downloadUrl.await().toString()
                    updates["profilePictureUrl"] = downloadUrl
                } else {
                    // It's already a URL, so we update it just in case
                    updates["profilePictureUrl"] = profilePictureUri
                }
            }

            firestore.collection("users").document(uid).update(updates).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkUpdateStreak(uid: String): Result<Boolean> {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            val userDto = document.toObject(UserDto::class.java) ?: return Result.failure(Exception("User not found"))
            
            val currentTimestamp = System.currentTimeMillis()
            val lastLogin = userDto.lastLoginDate
            
            val calendarCurrent = java.util.Calendar.getInstance()
            calendarCurrent.timeInMillis = currentTimestamp
            
            val calendarLast = java.util.Calendar.getInstance()
            calendarLast.timeInMillis = lastLogin
            
            val isSameDay = calendarCurrent.get(java.util.Calendar.YEAR) == calendarLast.get(java.util.Calendar.YEAR) &&
                            calendarCurrent.get(java.util.Calendar.DAY_OF_YEAR) == calendarLast.get(java.util.Calendar.DAY_OF_YEAR)
            
            if (isSameDay) {
                return Result.success(true)
            }
            
            calendarLast.add(java.util.Calendar.DAY_OF_YEAR, 1)
            val isNextDay = calendarCurrent.get(java.util.Calendar.YEAR) == calendarLast.get(java.util.Calendar.YEAR) &&
                            calendarCurrent.get(java.util.Calendar.DAY_OF_YEAR) == calendarLast.get(java.util.Calendar.DAY_OF_YEAR)
            
            var newStreak = userDto.streakDays
            if (isNextDay) {
                newStreak += 1
            } else {
                newStreak = 1
            }
            
            val updates = mapOf(
                "streakDays" to newStreak,
                "lastLoginDate" to currentTimestamp
            )
            
            firestore.collection("users").document(uid).update(updates).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markLessonCompleted(uid: String, lessonTitle: String): Result<Boolean> {
        return try {
            firestore.collection("users").document(uid)
                .update("completedLessons", com.google.firebase.firestore.FieldValue.arrayUnion(lessonTitle))
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun logout() {
        auth.signOut()
    }
}