package com.example.guitar_share.presentation.ui.components.bottom_navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

// La data class se mantiene igual
data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun BottomNavBar(navController: NavController) {
    val menuItems = listOf(
        BottomNavItem("Racha", "racha", Icons.Default.LocalFireDepartment),
        BottomNavItem("Librería", "library", Icons.Default.LibraryMusic),
        BottomNavItem("Home", "home", Icons.Default.Home),
        BottomNavItem("Foro", "forum", Icons.Default.Forum),
        // ¡IMPORTANTE! La ruta aquí debe ser la del grafo anidado
        BottomNavItem("Perfil", "profile_graph", Icons.Default.Person)
    )

    NavigationBar(
        containerColor = Color(0xFF1C1C1C)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        menuItems.forEach { screen ->
            NavigationBarItem(
                // Comprueba si el destino actual está dentro de la ruta del ítem.
                // Esto hace que el ícono de perfil se mantenga seleccionado
                // incluso cuando estás en "edit_profile".
                selected = currentDestination?.route?.startsWith(screen.route) == true,

                // --- INICIO DE LA CORRECCIÓN CLAVE ---
                onClick = {
                    navController.navigate(screen.route) {
                        // Hace pop hasta el destino inicial del grafo principal.
                        // Evita construir una pila de destinos muy grande cuando
                        // el usuario selecciona ítems.
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Evita múltiples copias del mismo destino en la pila.
                        launchSingleTop = true
                        // Restaura el estado al volver a navegar a un destino previamente seleccionado.
                        restoreState = true
                    }
                },
                // --- FIN DE LA CORRECCIÓN CLAVE ---

                icon = { Icon(screen.icon, contentDescription = screen.title, tint = Color.White) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFFE65100) // Color naranja para el indicador
                )
            )
        }
    }
}
