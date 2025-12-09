package com.example.guitar_share.presentation.screens.scores_library

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.guitar_share.presentation.components.bottom_navbar.BottomNavBar
import com.example.guitar_share.presentation.components.field.Field
import com.example.guitar_share.presentation.components.topbar.SharedTopBar
import com.example.guitar_share.presentation.screens.login.AuthViewModel
import com.example.guitar_share.presentation.theme.OrangePrimary
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddNewScore(
    navController: NavController,
    viewModel: ScoreLibraryViewModel = viewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    var titleInput by remember { mutableStateOf("") }
    var artistInput by remember { mutableStateOf("") }
    var genreInput by remember { mutableStateOf("") }
    var keyInput by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    var tagInput by remember { mutableStateOf("") }
    val selectedTags = remember { mutableStateListOf<String>() }
    var expandedTagMenu by remember { mutableStateOf(false) }

    val availableTags = listOf(
        "Rock", "Pop", "Metal", "Jazz", "Blues",
        "Clásica", "Acústica", "Eléctrica", "Fingerstyle",
        "Principiante", "Intermedio", "Avanzado", "Solo", "Riffs"
    )

    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
                title = "Añadir Partitura",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = { BottomNavBar(navController) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Field(
                    modifier = Modifier.fillMaxWidth(),
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    label = "Título de la partitura"
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Field(
                    modifier = Modifier.fillMaxWidth(),
                    value = artistInput,
                    onValueChange = { artistInput = it },
                    label = "Artista"
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Field(
                        modifier = Modifier.fillMaxWidth(),
                        value = genreInput,
                        onValueChange = { genreInput = it },
                        label = "Género"
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Field(
                        modifier = Modifier.fillMaxWidth(),
                        value = keyInput,
                        onValueChange = { keyInput = it },
                        label = "Clave"
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Selecciona partitura (Imagen):", fontWeight = FontWeight.Medium)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(
                            width = 2.dp,
                            color = if (selectedImageUri != null) OrangePrimary else Color.Gray.copy(
                                alpha = 0.3f
                            ),
                            shape = RoundedCornerShape(12.dp)
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Seleccionar imagen",
                                modifier = Modifier.size(48.dp),
                                tint = Color.Gray
                            )
                            Text(
                                text = "Toca para seleccionar imagen",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Etiquetas:", fontWeight = FontWeight.Medium)
                
                if (selectedTags.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        selectedTags.forEach { tag ->
                            InputChip(
                                selected = true,
                                onClick = { selectedTags.remove(tag) },
                                label = { Text(tag) },
                                trailingIcon = {
                                    Icon(Icons.Default.Close, contentDescription = "Eliminar", modifier = Modifier.size(16.dp))
                                },
                                colors = InputChipDefaults.inputChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Field(
                        modifier = Modifier.weight(1f),
                        value = tagInput,
                        onValueChange = { tagInput = it },
                        label = "Nueva etiqueta"
                    )

                    Box {
                        Button(
                            onClick = { expandedTagMenu = true },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.White
                            ),
                            modifier = Modifier.height(56.dp),
                            enabled = !isLoading,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Seleccionar etiqueta"
                            )
                        }

                        DropdownMenu(
                            expanded = expandedTagMenu,
                            onDismissRequest = { expandedTagMenu = false }
                        ) {
                            availableTags.forEach { tag ->
                                DropdownMenuItem(
                                    text = { Text(text = tag) },
                                    onClick = {
                                        if (!selectedTags.contains(tag)) {
                                            selectedTags.add(tag)
                                        }
                                        expandedTagMenu = false
                                    }
                                )
                            }
                            if (tagInput.isNotBlank() && !availableTags.contains(tagInput)) {
                                DropdownMenuItem(
                                    text = { Text(text = "Añadir \"$tagInput\"") },
                                    onClick = {
                                        if (!selectedTags.contains(tagInput)) {
                                            selectedTags.add(tagInput)
                                            tagInput = ""
                                        }
                                        expandedTagMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
                if (tagInput.isNotBlank()) {
                     Button(
                        onClick = {
                            if (!selectedTags.contains(tagInput)) {
                                selectedTags.add(tagInput)
                                tagInput = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Añadir")
                    }
                }
            }

            Column(
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        if (titleInput.isNotBlank() && artistInput.isNotBlank() && selectedImageUri != null) {
                            if (currentUser == null) {
                                scope.launch { snackbarHostState.showSnackbar("Error: Usuario no identificado") }
                                return@Button
                            }
                            
                            viewModel.uploadScore(
                                title = titleInput,
                                artist = artistInput,
                                genre = genreInput,
                                key = keyInput,
                                imageUri = selectedImageUri,
                                userId = currentUser.id,
                                tags = selectedTags.toList(),
                                onSuccess = {
                                    navController.popBackStack()
                                },
                                onError = { errorMsg ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(errorMsg)
                                    }
                                }
                            )
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Completa título, artista e imagen")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFFF05F22),
                        contentColor = Color.White
                    ),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    androidx.compose.material3.Text(
                        if (isLoading) "Subiendo..." else "Añadir partitura", 
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFF605D5D),
                        contentColor = Color.White
                    ),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    androidx.compose.material3.Text(
                        "Cancelar",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
