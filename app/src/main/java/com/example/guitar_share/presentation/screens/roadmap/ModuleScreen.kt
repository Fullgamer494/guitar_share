package com.example.guitar_share.presentation.screens.roadmap

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.guitar_share.R
import com.example.guitar_share.presentation.components.bottom_navbar.BottomNavBar
import com.example.guitar_share.presentation.components.topbar.SharedTopBar

@Composable
fun ModuleScreen(
    navController: NavController,
    moduleName: String,
    authViewModel: com.example.guitar_share.presentation.screens.login.AuthViewModel = org.koin.androidx.compose.koinViewModel()
) {
    val lessons = RoadmapData.getLessonsForModule(moduleName)
    val currentUser = authViewModel.currentUserData

    Scaffold(
        topBar = {
            SharedTopBar(
                title = "Módulo: $moduleName",
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(40.dp) // Spacing between rows
            ) {
                // We map indices to alignments to simulate a zigzag/curve:
                // 0 -> Center
                // 1 -> End (Right)
                // 2 -> Center
                // 3 -> Start (Left)
                // 4 -> Center
                itemsIndexed(lessons) { index, lesson ->
                    val alignment = when (index % 4) {
                        0 -> Alignment.CenterHorizontally
                        1 -> Alignment.End
                        2 -> Alignment.CenterHorizontally
                        3 -> Alignment.Start
                        else -> Alignment.CenterHorizontally
                    }
                    
                    val isCompleted = currentUser?.completedLessons?.contains(lesson.title) == true

                    // For decorations: if node is Right, put image Left. If node is Left, put image Right.
                    // If Center, maybe smaller decorations or none.
                    
                    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp)) {
                         // Decoration Image (Optional, behind or beside)
                        // Image Alignment Logic
                        // 0 (Center) -> Image Left
                        // 1 (Right)  -> Image Left
                        // 2 (Center) -> Image Right
                        // 3 (Left)   -> Image Right
                        val imageAlignment = if (index % 4 < 2) Alignment.CenterStart else Alignment.CenterEnd
                        val imageOffset = if (imageAlignment == Alignment.CenterStart) (-20).dp else 20.dp
                        
                        Image(
                             painter = painterResource(id = lesson.imageRes),
                             contentDescription = null,
                             modifier = Modifier
                                 .align(imageAlignment)
                                 .size(100.dp) // Increased size
                                 .offset(x = imageOffset)
                        )

                        // Lesson Node
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .align(when(alignment) {
                                    Alignment.Start -> Alignment.CenterStart
                                    Alignment.End -> Alignment.CenterEnd
                                    else -> Alignment.Center
                                })
                                .clickable { navController.navigate("lesson/${lesson.title}") }
                        ) {
                            LessonNode(isCompleted = isCompleted)
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = lesson.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(100.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LessonNode(isCompleted: Boolean) {
    // Colors from request
    val Orange = Color(0xFFEF6C00) // Deep Orange
    val Grey = Color.Gray
    
    val backgroundColor = if (isCompleted) Orange else Grey
    
    // Using a shape similar to the image (Oval/Circle button)
    Surface(
        shape = CircleShape, // Use standard Circle (or RoundedCornerShape(50) for oval)
        color = Color(0xFFE0E0E0), // Bottom "3D" part (lighter grey/white edge)
        modifier = Modifier
            .width(80.dp)
            .height(50.dp) // Oval shape
    ) {
        // Inner colored part to create 3D effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 6.dp) // Lift up the top part
                .background(backgroundColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
             // Optional icon (Star for completed, Lock for locked?)
             // Keeping it clean as per image for now, maybe simple highlight
        }
    }
}
