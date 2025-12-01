package com.example.guitar_share.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.guitar_share.presentation.ui.screens.home.HomeScreen
import com.example.guitar_share.presentation.ui.screens.profile.EditProfileScreen
import com.example.guitar_share.presentation.ui.screens.profile.ProfileScreen
import com.example.guitar_share.presentation.ui.screens.profile.viewmodel.ProfileViewModel

@Composable
fun NavManager() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home" // Ruta de inicio de la app
    ) {
        // Rutas principales de tu app
        composable("home") { HomeScreen(navController) }
        composable("racha") {} // Temporalmente, pon una pantalla vacía
        composable("library") {  } // Temporalmente, pon una pantalla vacía
        composable("forum") { } // Temporalmente, pon una pantalla vacía

        // --- INICIO DE LA CORRECCIÓN ---
        // Se crea un grafo de navegación ANIDADO para todo lo relacionado con el perfil.
        // La ruta de este grafo es "profile_graph".
        navigation(
            startDestination = "profile_main", // La pantalla de inicio de este sub-menú
            route = "profile_graph"
        ) {
            // Pantalla principal del perfil, dentro del grafo anidado
            composable("profile_main") {
                // Pide el ViewModel compartido para este grafo.
                val profileViewModel = it.sharedViewModel<ProfileViewModel>(navController)
                ProfileScreen(
                    navController = navController,
                    viewModel = profileViewModel
                )
            }

            // Pantalla de edición, también dentro del grafo
            composable("edit_profile") {
                // Pide la MISMA instancia del ViewModel compartido.
                val profileViewModel = it.sharedViewModel<ProfileViewModel>(navController)
                EditProfileScreen(
                    navController = navController,
                    viewModel = profileViewModel
                )
            }
        }
        // --- FIN DE LA CORRECCIÓN ---
    }
}

// Función de extensión para simplificar la obtención del ViewModel compartido.
// ¡Pon esto en el mismo archivo, fuera del @Composable NavManager!
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}
