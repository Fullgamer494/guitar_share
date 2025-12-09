package com.example.guitar_share.presentation.screens.roadmap

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.guitar_share.presentation.components.bottom_navbar.BottomNavBar
import com.example.guitar_share.presentation.components.topbar.SharedTopBar
import com.example.guitar_share.presentation.screens.login.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RoadmapScreen(
    navController: NavController,
    authViewModel: AuthViewModel = koinViewModel()
) {
    // Ensure user data is loaded to get level
    LaunchedEffect(Unit) {
        authViewModel.fetchCurrentUser()
    }
    val currentUser = authViewModel.currentUserData
    // Default to "Principiante" if null or not set
    val userLevel = currentUser?.guitarLevel ?: "Principiante"
    val modules = RoadmapData.getModulesForLevel(userLevel)

    Scaffold(
        topBar = {
            SharedTopBar(title = "Nivel: $userLevel")
        },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.Black) // As per image reference (dark bg)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(modules) { module ->
                    RoadmapModuleCard(module = module) { moduleTitle ->
                        navController.navigate("module/$moduleTitle")
                    }
                }
            }
        }
    }
}

@Composable
fun RoadmapModuleCard(module: RoadmapModule, onClick: (String) -> Unit) {
    Surface(
        color = module.color,
        shape = RoundedCornerShape(8.dp),
        onClick = { onClick(module.title) },
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp) // Approximate height based on image
    ) {
        // Module Content
        Box(modifier = Modifier.fillMaxSize()) {
            
            // Title
            Text(
                text = module.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 28.sp, // Large bold text
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 24.dp)
                    .fillMaxWidth(0.6f) // Take up ~60% of width
            )

            // Image on the right
            Image(
                painter = painterResource(id = module.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(100.dp) // Adjust size as needed
                    .padding(end = 8.dp, bottom = 0.dp) 
                    // Note: In real app, might need offset or specific assets to match the 'peeking' look
            )
        }
    }
}
