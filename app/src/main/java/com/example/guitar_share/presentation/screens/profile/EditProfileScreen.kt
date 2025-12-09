package com.example.guitar_share.presentation.screens.profile

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.guitar_share.presentation.components.field.Field
import com.example.guitar_share.presentation.screens.login.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel()
) {
    val user = viewModel.currentUserData
    
    // Force fetch user data when screen opens
    LaunchedEffect(Unit) {
        viewModel.fetchCurrentUser()
    }

    // Initialize state with user data, using LaunchedEffect to update when user data loads
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var guitarLevel by remember { mutableStateOf("") }
    var profilePictureUrl by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageSourceDialog by remember { mutableStateOf(false) }

    LaunchedEffect(user) {
        user?.let {
            username = it.username
            email = it.email
            guitarLevel = it.guitarLevel
            profilePictureUrl = it.profilePictureUrl
        }
    }

    // Camera Launcher
    val context = LocalContext.current
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            try {
                val file = java.io.File(context.cacheDir, "profile_temp_${System.currentTimeMillis()}.jpg")
                val fos = java.io.FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
                selectedImageUri = Uri.fromFile(file)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    // Gallery Launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri = it }
    }

    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Seleccionar imagen") },
            text = { Text("¿De dónde quieres obtener la imagen?") },
            confirmButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    galleryLauncher.launch("image/*")
                }) {
                    Text("Galería")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showImageSourceDialog = false
                    cameraLauncher.launch()
                }) {
                    Text("Cámara")
                }
            }
        )
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            com.example.guitar_share.presentation.components.topbar.SharedTopBar(
                title = "Editar Perfil",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Picture with Edit Overlay
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = selectedImageUri ?: profilePictureUrl.ifEmpty { "https://i.pinimg.com/736x/ba/94/64/ba9464145eba8762f6286a3c8387c951.jpg" },
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                
                SmallFloatingActionButton(
                    onClick = { showImageSourceDialog = true },
                    containerColor = Color(0xFFF05F22),
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Cambiar foto")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Editable Fields
            Field(
                value = username,
                onValueChange = { username = it },
                label = "Nombre de usuario",
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Field(
                value = email,
                onValueChange = { email = it },
                label = "Correo electrónico",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Disabled Field
            OutlinedTextField(
                value = guitarLevel,
                onValueChange = {},
                label = { Text("Nivel de guitarra") },
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.White, // Make text visible even if disabled
                    disabledBorderColor = Color.Gray,
                    disabledLabelColor = Color.Gray,
                    disabledContainerColor = Color(0xFF1E1E1E)
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            // Action Buttons
            if (viewModel.isLoading) {
                CircularProgressIndicator(color = Color(0xFFF05F22))
            } else {
                Button(
                    onClick = {
                        viewModel.updateUser(
                            username = username,
                            email = email,
                            profilePictureUri = selectedImageUri?.toString() ?: profilePictureUrl,
                            onSuccess = {
                                navController.popBackStack()
                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF05F22)),
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Editar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF605D5D)),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancelar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
