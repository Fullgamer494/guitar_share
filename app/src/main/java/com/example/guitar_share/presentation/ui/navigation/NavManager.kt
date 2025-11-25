package com.example.guitar_share.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guitar_share.presentation.ui.screens.home.HomeScreen
import com.example.guitar_share.presentation.ui.screens.scores_library.AddNewScore
import com.example.guitar_share.presentation.ui.screens.scores_library.ScoreLibraryScreen

@Composable
fun NavManager(){
    val navController = rememberNavController()

    NavHost(
        navController,
        startDestination = "home"
    ){

        composable("library"){
            ScoreLibraryScreen(navController)
        }

        composable("addNewScore"){
            AddNewScore(navController)
        }


        composable("home"){
            HomeScreen(navController)
        }

        composable("forum"){
        }

        composable("profile"){
        }
    }
}