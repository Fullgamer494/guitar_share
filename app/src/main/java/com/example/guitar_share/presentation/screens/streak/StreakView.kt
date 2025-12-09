package com.example.guitar_share.presentation.screens.streak

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.guitar_share.R
import com.example.guitar_share.presentation.components.bottom_navbar.BottomNavBar
import com.example.guitar_share.presentation.screens.login.AuthViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@Composable
fun StreakView(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel()
) {
    val user = viewModel.currentUserData
    
    LaunchedEffect(Unit) {
        viewModel.fetchCurrentUser()
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        containerColor = Color.Black
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E1E1E))
                    .padding(16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Logros y racha",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Streak Number with Gradient
            val streakDays = user?.streakDays ?: 0
            Text(
                text = streakDays.toString(),
                style = TextStyle(
                    fontSize = 120.sp,
                    fontWeight = FontWeight.ExtraBold,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFC107), // Amber/Yellow
                            Color(0xFFF05F22)  // Orange
                        )
                    )
                )
            )

            Text(
                text = "Días de racha",
                color = Color(0xFFF05F22),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Days of Week Indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val days = listOf("Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do")
                val currentDayIndex = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 5) % 7 // Adjust so Monday is 0
                
                days.forEachIndexed { index, day ->
                    DayIndicator(
                        day = day,
                        isActive = index <= currentDayIndex, // Simple logic: active if passed or today
                        isToday = index == currentDayIndex
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.mascot_streak),
                contentDescription = "Mascot",
                modifier = Modifier
                    .size(400.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun DayIndicator(day: String, isActive: Boolean, isToday: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (isActive) Color(0xFFF05F22) else Color(0xFF605D5D)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = if (isActive) Color.White else Color.Black.copy(alpha = 0.5f),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = day,
            color = if (isActive) Color.Gray else Color.DarkGray,
            fontWeight = FontWeight.Bold
        )
    }
}
