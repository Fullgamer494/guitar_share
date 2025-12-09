package com.example.guitar_share.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guitar_share.presentation.screens.comments.CommentsScreen
import org.koin.androidx.compose.koinViewModel
// Importaciones de tus pantallas existentes
import com.example.guitar_share.presentation.screens.forum.ForumPostScreen
import com.example.guitar_share.presentation.screens.forum.ForumSocialScreen
import com.example.guitar_share.presentation.screens.home.HomeScreen
import com.example.guitar_share.presentation.screens.scores_library.AddNewScore
import com.example.guitar_share.presentation.screens.scores_library.ScoreLibraryScreen
// Importaciones NUEVAS para Auth (Asegúrate de que coincidan con tu paquete)
import com.example.guitar_share.presentation.screens.login.AuthViewModel
import com.example.guitar_share.presentation.screens.login.LoginScreen
import com.example.guitar_share.presentation.screens.profile.ProfileScreen
import com.example.guitar_share.presentation.screens.profile.EditProfileScreen
import com.example.guitar_share.presentation.screens.roadmap.ModuleScreen
import com.example.guitar_share.presentation.screens.register.RegisterScreen
import com.example.guitar_share.presentation.screens.roadmap.RoadmapScreen
import com.example.guitar_share.presentation.screens.lesson.LessonScreen

@Composable
fun NavManager() {
    val navController = rememberNavController()

    // Instanciamos el ViewModel aquí para que sobreviva a la navegación
    // y pueda decidir el estado inicial (si el usuario ya está logueado)
    val authViewModel: AuthViewModel = koinViewModel()

    // Lógica dinámica: Si ya está logueado, vamos a Home, si no, a Login
    val startDestination = if (authViewModel.navigateToHome) "home" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // --- ZONA DE AUTENTICACIÓN ---

        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = {
                    // Al entrar, borramos el historial para que no pueda volver al login con "Atrás"
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateToLogin = {
                    navController.popBackStack() // Volver atrás
                },
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            RoadmapScreen(navController = navController, authViewModel = authViewModel)
        }

        composable("module/{moduleName}") { backStackEntry ->
            val moduleName = backStackEntry.arguments?.getString("moduleName") ?: return@composable
            ModuleScreen(navController = navController, moduleName = moduleName)
        }

        composable("lesson/{lessonTitle}") { backStackEntry ->
            val lessonTitle = backStackEntry.arguments?.getString("lessonTitle") ?: return@composable
            LessonScreen(navController = navController, lessonTitle = lessonTitle)
        }

        composable("library") {
            ScoreLibraryScreen(navController)
        }

        composable("addNewScore") {
            AddNewScore(navController)
        }

        composable("forum") {
            ForumSocialScreen(navController)
        }

        composable("create_post") {
            ForumPostScreen(navController)
        }

        composable("profile") {
            ProfileScreen(
                navController = navController,
                viewModel = authViewModel,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("edit_profile") {
            EditProfileScreen(navController, viewModel = authViewModel)
        }

        composable("racha") {
            com.example.guitar_share.presentation.screens.streak.StreakView(navController, viewModel = authViewModel)
        }

        composable("comments/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: return@composable
            CommentsScreen(
                navController = navController,
                postId = postId
            )
        }
    }
}