package com.example.guitar_share.presentation.screens.lesson

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.guitar_share.presentation.components.bottom_navbar.BottomNavBar
import com.example.guitar_share.presentation.components.topbar.SharedTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun LessonScreen(
    navController: NavController,
    lessonTitle: String,
    viewModel: LessonViewModel = koinViewModel(),
    authViewModel: com.example.guitar_share.presentation.screens.login.AuthViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(lessonTitle) {
        viewModel.loadLesson(lessonTitle)
        authViewModel.markLessonCompleted(lessonTitle)
    }

    Scaffold(
        topBar = {
            SharedTopBar(
                title = "Lección: $lessonTitle",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.Black)
        ) {
            when (val currentState = state) {
                is LessonState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is LessonState.Error -> {
                    Text(
                        text = currentState.message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is LessonState.Success -> {
                    val content = currentState.content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // Main Title
                        Text(
                            text = content.title,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Spacer(modifier = Modifier.height(24.dp))

                        // Orange Section Header "1. [Subtitle]"
                        Surface(
                            color = Color(0xFFEF6C00), // Orange
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "1. ${content.subtitle}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Image / Diagram
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White), // White bg for diagram visibility
                            contentAlignment = Alignment.Center
                        ) {
                             // If content.imageUrl is Int
                             if (content.imageUrl is Int) {
                                 Image(
                                     painter = painterResource(id = content.imageUrl),
                                     contentDescription = null,
                                     contentScale = ContentScale.Fit, // Fit to show diagram clearly
                                     modifier = Modifier.fillMaxSize().padding(16.dp)
                                 )
                             }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Body Text
                        Text(
                            text = content.body,
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White, lineHeight = 24.sp)
                        )
                        
                        // Extra Chord Details if available
                        if (content.chordName != null) {
                             Spacer(modifier = Modifier.height(24.dp))
                             Text(
                                text = "Detalles Técnicos (API):",
                                style = MaterialTheme.typography.titleMedium.copy(color = Color.Yellow)
                             )
                             Text(
                                text = "Nombre: ${content.chordName}\nDigitación: ${content.fingering}\nCuerdas: ${content.strings}",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.LightGray)
                             )
                        }
                    }
                }
            }
        }
    }
}
