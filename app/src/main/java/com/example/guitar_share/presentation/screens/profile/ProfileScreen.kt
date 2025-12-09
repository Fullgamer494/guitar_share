package com.example.guitar_share.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.guitar_share.presentation.components.bottom_navbar.BottomNavBar
import com.example.guitar_share.presentation.components.topbar.SharedTopBar
import com.example.guitar_share.presentation.screens.login.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel(),
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.fetchCurrentUser()
    }

    val user = viewModel.currentUserData
    val OrangeAccent = Color(0xFFF05F22)

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            SharedTopBar(title = "Perfil")
        },
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { innerPadding ->

        if (user == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = OrangeAccent)
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Modern Profile Header
                Box(contentAlignment = Alignment.BottomEnd) {
                    AsyncImage(
                        model = user.profilePictureUrl.ifEmpty { "https://i.pinimg.com/736x/ba/94/64/ba9464145eba8762f6286a3c8387c951.jpg" },
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(Color.DarkGray),
                        contentScale = ContentScale.Crop
                    )

                    SmallFloatingActionButton(
                        onClick = { navController.navigate("edit_profile") },
                        containerColor = OrangeAccent,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(40.dp)
                            .offset(x = 0.dp, y = 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar perfil",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = user.username,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Info Cards
                ProfileInfoCard(label = "Correo electrónico", value = user.email)
                Spacer(modifier = Modifier.height(16.dp))
                ProfileInfoCard(label = "Nivel de guitarra", value = user.guitarLevel)
                
                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        viewModel.logout()
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF605D5D)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cerrar Sesión", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileInfoCard(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}