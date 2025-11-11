package com.example.guitar_share.presentation.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.guitar_share.presentation.ui.components.bottom_navbar.BottomNavBar

@Composable
fun HomeScreen(navController: NavController){
    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ){
            Text("Vista de Home")
        }
    }
}