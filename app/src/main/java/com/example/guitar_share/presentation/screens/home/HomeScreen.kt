package com.example.guitar_share.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.guitar_share.presentation.components.bottom_navbar.BottomNavBar
import com.example.guitar_share.presentation.components.topbar.SharedTopBar

@Composable
fun HomeScreen(navController: NavController){
    Scaffold(
        topBar = {
            SharedTopBar(title = "Inicio")
        },
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