package com.example.guitar_share.presentation.components.bottom_navbar

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
        containerColor = Color(0xFF111111),
        tonalElevation = 8.dp
    ) {
        val itemColors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Transparent,
            selectedIconColor = Color(0xFFF05F22),
            unselectedIconColor = Color.White
        )

        NavigationBarItem(
            icon = { 
                Icon(
                    Icons.Filled.LocalFireDepartment, 
                    contentDescription = "Score",
                    modifier = Modifier.size(26.dp)
                ) 
            },
            selected = currentRoute == "racha",
            onClick = {
                navController.navigate("racha") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            colors = itemColors
        )

        NavigationBarItem(
            icon = { 
                Icon(
                    Icons.Filled.LibraryMusic, 
                    contentDescription = "Library",
                    modifier = Modifier.size(26.dp)
                ) 
            },
            selected = currentRoute == "library",
            onClick = {
                navController.navigate("library") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            colors = itemColors
        )

        NavigationBarItem(
            icon = { 
                Icon(
                    Icons.Filled.Home, 
                    contentDescription = "Home",
                    modifier = Modifier.size(26.dp)
                ) 
            },
            selected = currentRoute == "home",
            onClick = {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            colors = itemColors
        )

        NavigationBarItem(
            icon = { 
                Icon(
                    Icons.Filled.Comment, 
                    contentDescription = "Forum",
                    modifier = Modifier.size(26.dp)
                ) 
            },
            selected = currentRoute == "forum",
            onClick = {
                navController.navigate("forum") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            colors = itemColors
        )

        NavigationBarItem(
            icon = { 
                Icon(
                    Icons.Filled.AccountCircle, 
                    contentDescription = "Profile",
                    modifier = Modifier.size(32.dp)
                ) 
            },
            selected = currentRoute == "profile",
            onClick = {
                navController.navigate("profile") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            colors = itemColors
        )
    }
}