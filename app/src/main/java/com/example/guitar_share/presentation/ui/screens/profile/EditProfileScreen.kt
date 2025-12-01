package com.example.guitar_share.presentation.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.guitar_share.presentation.ui.screens.profile.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel // Recibe el ViewModel
) {
    // Observamos el estado del ViewModel para obtener los datos iniciales
    val uiState by viewModel.uiState.collectAsState()

    // Estados locales para los campos de texto, para que el usuario pueda editarlos.
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var guitarLevel by remember { mutableStateOf("") }

    // --- INICIO DE LA MODIFICACIÓN (1/3) ---
    // Estado para controlar si el menú desplegable está expandido o no.
    var isGuitarLevelMenuExpanded by remember { mutableStateOf(false) }
    // Lista de opciones para el menú.
    val guitarLevelOptions = listOf("Principiante", "Intermedio", "Avanzado")
    // --- FIN DE LA MODIFICACIÓN (1/3) ---

    // `LaunchedEffect` se usa para actualizar los estados locales
    // una sola vez cuando los datos del ViewModel están listos.
    LaunchedEffect(uiState) {
        username = uiState.username
        email = uiState.email
        guitarLevel = uiState.guitarLevel
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1C1C1C))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Campo para Nombre de usuario (sin cambios)
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth(),
                // ... tus colores y otras propiedades
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Campo para Correo electrónico (sin cambios)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                // ... tus colores y otras propiedades
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- INICIO DE LA MODIFICACIÓN (2/3) ---
            // Reemplazamos el OutlinedTextField por un ExposedDropdownMenuBox.
            ExposedDropdownMenuBox(
                expanded = isGuitarLevelMenuExpanded,
                onExpandedChange = { isGuitarLevelMenuExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Este es el campo de texto que se muestra.
                // Es parte del menú y no es directamente editable por el teclado.
                OutlinedTextField(
                    value = guitarLevel,
                    onValueChange = {}, // Se deja vacío porque el cambio se maneja en el menú.
                    readOnly = true, // Impide que el usuario escriba directamente.
                    label = { Text("Nivel de guitarra") },
                    trailingIcon = {
                        // El icono de flecha que indica que es un menú desplegable.
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isGuitarLevelMenuExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                        // Colores para que coincida con tu tema oscuro
                        focusedBorderColor = Color(0xFFE65100),
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color(0xFFE65100),
                        unfocusedLabelColor = Color.Gray,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedTrailingIconColor = Color(0xFFE65100),
                        unfocusedTrailingIconColor = Color.Gray
                    ),
                    modifier = Modifier
                        .menuAnchor() // Ancla el menú al campo de texto.
                        .fillMaxWidth()
                )

                // Este es el menú que se despliega con las opciones.
                ExposedDropdownMenu(
                    expanded = isGuitarLevelMenuExpanded,
                    onDismissRequest = { isGuitarLevelMenuExpanded = false }
                ) {
                    // Creamos un ítem de menú para cada opción en nuestra lista.
                    guitarLevelOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                guitarLevel = option // Actualiza el estado con la opción seleccionada.
                                isGuitarLevelMenuExpanded = false // Cierra el menú.
                            }
                        )
                    }
                }
            }
            // --- FIN DE LA MODIFICACIÓN (2/3) ---

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    // --- INICIO DE LA MODIFICACIÓN (3/3) ---
                    // La lógica aquí no cambia, pero ahora enviará el valor
                    // seleccionado del menú.
                    viewModel.saveProfileChanges(
                        newUsername = username,
                        newEmail = email,
                        newGuitarLevel = guitarLevel
                    )
                    // --- FIN DE LA MODIFICACIÓN (3/3) ---
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
            ) {
                Text("Guardar Cambios", fontWeight = FontWeight.Bold)
            }
        }
    }
}
