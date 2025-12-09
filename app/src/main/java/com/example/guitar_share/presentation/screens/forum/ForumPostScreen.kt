package com.example.guitar_share.presentation.screens.forum

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.guitar_share.presentation.components.field.Field
import com.example.guitar_share.presentation.components.bottom_navbar.BottomNavBar
import com.example.guitar_share.presentation.components.topbar.SharedTopBar
import com.example.guitar_share.presentation.screens.login.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumPostScreen(
    navController: NavController,
    viewModel: ForumViewModel = viewModel(),
    authViewModel: AuthViewModel = koinViewModel() // Inject AuthViewModel
) {
    var titleInput by remember { mutableStateOf("") }
    var bodyInput by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Ensure user data is loaded
    LaunchedEffect(Unit) {
        authViewModel.fetchCurrentUser()
    }
    val currentUser = authViewModel.currentUserData

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Scaffold(
        topBar = {
            SharedTopBar(
                title = "Crear publicación",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = { BottomNavBar(navController) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Field(
                value = titleInput,
                onValueChange = { titleInput = it },
                label = "Título de la publicación",
                modifier = Modifier.fillMaxWidth()
            )

            Field(
                value = bodyInput,
                onValueChange = { bodyInput = it },
                label = "Escribe aquí lo que piensas...",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                singleLine = false,
                minLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Añadir imagen (opcional):",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable(enabled = !isLoading) {
                        imagePickerLauncher.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = "Imagen seleccionada",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Seleccionar imagen",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Toca para subir",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    if (titleInput.isBlank() || bodyInput.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("El título y el cuerpo son obligatorios")
                        }
                        return@Button
                    }
                    
                    if (currentUser == null) {
                         scope.launch {
                            snackbarHostState.showSnackbar("Error: Usuario no identificado")
                        }
                        return@Button
                    }

                    isLoading = true
                    viewModel.createPostWithImage(
                        title = titleInput,
                        body = bodyInput,
                        authorName = currentUser.username,
                        userId = currentUser.id,
                        authorProfileUrl = currentUser.profilePictureUrl,
                        tag = "General", // Could be selectable
                        imageUri = selectedImageUri,
                        onSuccess = {
                            isLoading = false
                            navController.popBackStack()
                        },
                        onError = { error ->
                            isLoading = false
                            scope.launch {
                                snackbarHostState.showSnackbar(error)
                            }
                        }
                    )
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFFF05F22),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp)
                    )
                }
                androidx.compose.material3.Text(
                    if (isLoading) "Publicando..." else "Publicar",
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                onClick = { navController.popBackStack() },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFF605D5D)),
                shape = RoundedCornerShape(12.dp)
            ) {
                androidx.compose.material3.Text(
                    "Cancelar",
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}