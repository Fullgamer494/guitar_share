package com.example.guitar_share.presentation.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
// --- INICIO DE LA CORRECCIÓN 1/2 ---
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
// --- FIN DE LA CORRECCIÓN 1/2 ---
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.guitar_share.presentation.ui.components.bottom_navbar.BottomNavBar
// --- INICIO DE LA CORRECCIÓN 2/2 ---
import com.example.guitar_share.presentation.ui.screens.profile.viewmodel.ProfileViewModel
// --- FIN DE LA CORRECCIÓN 2/2 ---

// Si tenías un `data class User` aquí, puedes borrarlo.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel // Recibe la instancia del ViewModel
) {
    // Observamos el estado de la UI desde el ViewModel.
    // `collectAsState` convierte el StateFlow en un State que Compose puede observar.
    // Cada vez que los datos en `viewModel.uiState` cambien, `uiState` se actualizará
    // y la pantalla se recompondrá automáticamente.
    val uiState by viewModel.uiState.collectAsState()
    val orangeAccent = Color(0xFFE65100)

    Scaffold(
        containerColor = Color.Black,
        topBar = { /* ... Tu TopAppBar se mantiene igual ... */ },
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    // La imagen ahora toma la URL del estado del ViewModel
                    Image(
                        painter = rememberAsyncImagePainter(model = uiState.profileImageUrl),
                        contentDescription = "Foto de perfil de ${uiState.username}",
                        modifier = Modifier.size(140.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    SmallFloatingActionButton(
                        onClick = {
                            // Navegación simple, ya no se pasan argumentos por la ruta
                            navController.navigate("edit_profile")
                        },
                        containerColor = orangeAccent,
                        contentColor = Color.White,
                        shape = CircleShape,
                        modifier = Modifier.size(45.dp).offset(x = (-5).dp, y = (-5).dp)
                    ) {
                        Icon(Icons.Filled.Edit, "Editar", modifier = Modifier.size(24.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // La UI ahora muestra los datos directamente del `uiState`
            ProfileInfoItem(label = "Nombre de usuario:", value = uiState.username)
            Spacer(modifier = Modifier.height(24.dp))
            ProfileInfoItem(label = "Correo electrónico:", value = uiState.email)
            Spacer(modifier = Modifier.height(24.dp))
            ProfileInfoItem(label = "Nivel de guitarra:", value = uiState.guitarLevel)
        }
    }
}

// El composable ProfileInfoItem no necesita cambios.
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
