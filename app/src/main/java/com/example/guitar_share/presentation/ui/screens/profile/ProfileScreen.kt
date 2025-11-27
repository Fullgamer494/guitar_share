package com.example.guitar_share.presentation.ui.screens.profile


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.guitar_share.presentation.ui.components.bottom_navbar.BottomNavBar

data class User(
    val username: String,
    val email: String,
    val guitarLevel: String,
    val profileImageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    // Datos simulados basados en tu imagen
    val user = User(
        username = "Fullgamer494",
        email = "fullgamer494@hotmail.com",
        guitarLevel = "Principiante",
        profileImageUrl = "https://i.pinimg.com/736x/ba/94/64/ba9464145eba8762f6286a3c8387c951.jpg" // URL de ejemplo (mono animado)
    )

    // Definimos el color naranja del botón de editar
    val OrangeAccent = Color(0xFFE65100)

    Scaffold(
        containerColor = Color.Black, // Fondo negro general
        topBar = {
            TopAppBar(
                title = {
                    // Box para alinear el texto a la derecha
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Text(
                            text = "Perfil",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1C1C1C) // Un gris muy oscuro para la barra superior
                )
            )
        },
        bottomBar = {
            // Mantenemos tu componente de navegación
            BottomNavBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.Start // Alineación general a la izquierda
        ) {

            // SECCIÓN DE FOTO DE PERFIL (Centrada)
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {

                    // Botón de editar superpuesto (Círculo naranja)
                    SmallFloatingActionButton(
                        onClick = { /* Acción editar */ },
                        containerColor = OrangeAccent,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(45.dp).offset(x = (-5).dp, y = (-5).dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // SECCIÓN DE DATOS
            // 1. Nombre de usuario
            ProfileInfoItem(label = "Nombre de usuario:", value = user.username)

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Correo electrónico
            ProfileInfoItem(label = "Correo electrónico:", value = user.email)

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Nivel de guitarra
            ProfileInfoItem(label = "Nivel de guitarra:", value = user.guitarLevel)
        }
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            color = Color.Gray, 
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        )
    }
}