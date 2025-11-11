package com.example.guitar_share.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guitar_share.presentation.ui.screens.forum.ForumScreen
import com.example.guitar_share.presentation.ui.screens.home.HomeScreen

@Composable
fun NavManager(){
    val navController = rememberNavController()

    NavHost(
        navController,
        startDestination = "home"
    ){
        composable("score"){

        }

        composable("library"){

        }

        composable("home"){
            HomeScreen(navController)
        }

        composable("forum"){
            ForumScreen(navController)
        }

        composable("profile"){
        }
    }
}