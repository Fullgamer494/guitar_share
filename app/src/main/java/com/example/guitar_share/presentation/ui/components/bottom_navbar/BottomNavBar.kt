package com.example.guitar_share.presentation.ui.components.bottom_navbar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clip(RoundedCornerShape(24.dp)),
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.LocalFireDepartment, contentDescription = "Score") },
            label = { Text("Racha") },
            selected = currentRoute == "racha",
            onClick = {
                navController.navigate("racha") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.LibraryMusic, contentDescription = "Library") },
            label = { Text("Librería") },
            selected = currentRoute == "library",
            onClick = {
                navController.navigate("library") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.Comment, contentDescription = "Forum") },
            label = { Text("Foro") },
            selected = currentRoute == "forum",
            onClick = {
                navController.navigate("forum") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Profile") },
            label = { Text("Perfil") },
            selected = currentRoute == "profile",
            onClick = {
                navController.navigate("profile") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }
        )
    }
}