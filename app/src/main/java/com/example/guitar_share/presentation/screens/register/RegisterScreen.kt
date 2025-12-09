package com.example.guitar_share.presentation.screens.register

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.guitar_share.presentation.components.field.Field
import com.example.guitar_share.presentation.components.topbar.SharedTopBar
import com.example.guitar_share.presentation.screens.login.AuthViewModel
import kotlinx.coroutines.launch

enum class RegisterStep {
    FORM, IMAGE
}

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var currentStep by remember { mutableStateOf(RegisterStep.FORM) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Form State
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var guitarLevel by remember { mutableStateOf("Principiante") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    var passwordVisible by remember { mutableStateOf(false) }

    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.errorMessage = null
        }
    }

    LaunchedEffect(viewModel.navigateToHome) {
        if (viewModel.navigateToHome) {
            onRegisterSuccess()
        }
    }

    Scaffold(
        topBar = {
            SharedTopBar(
                title = if (currentStep == RegisterStep.FORM) "Crear Cuenta" else "Tu Estilo",
                showBackButton = currentStep == RegisterStep.IMAGE,
                onBackClick = { currentStep = RegisterStep.FORM }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            AnimatedContent(targetState = currentStep, label = "RegisterStep") { step ->
                when (step) {
                    RegisterStep.FORM -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Field(
                                value = username,
                                onValueChange = { username = it },
                                label = "Nombre de usuario",
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Field(
                                value = email,
                                onValueChange = { email = it },
                                label = "Correo electrónico",
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Field(
                                value = password,
                                onValueChange = { password = it },
                                label = "Contraseña",
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = null
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Guitar Level Dropdown (Simplified)
                            var expanded by remember { mutableStateOf(false) }
                            val levels = listOf("Principiante", "Intermedio", "Avanzado")
                            
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = guitarLevel,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Nivel de guitarra") },
                                    trailingIcon = {
                                        IconButton(onClick = { expanded = !expanded }) {
                                            Icon(Icons.Default.Person, contentDescription = "Select")
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    levels.forEach { level ->
                                        DropdownMenuItem(
                                            text = { Text(level) },
                                            onClick = {
                                                guitarLevel = level
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    if (username.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                                        currentStep = RegisterStep.IMAGE
                                    } else {
                                        scope.launch { snackbarHostState.showSnackbar("Completa todos los campos") }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFFF05F22))
                            ) {
                                androidx.compose.material3.Text(
                                    "Siguiente", 
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, 
                                    fontSize = 16.sp
                                )
                            }

                            TextButton(onClick = onNavigateToLogin) {
                                Text("¿Ya tienes cuenta? Inicia sesión")
                            }
                        }
                    }
                    RegisterStep.IMAGE -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("Sube tu foto de perfil", style = MaterialTheme.typography.titleLarge)
                            Spacer(modifier = Modifier.height(24.dp))

                            val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
                                selectedImageUri = it
                            }

                            Box(
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                                    .clickable { launcher.launch("image/*") },
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedImageUri != null) {
                                    Image(
                                        painter = rememberAsyncImagePainter(selectedImageUri),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(64.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    viewModel.register(email, password, username, guitarLevel, selectedImageUri?.toString())
                                },
                                enabled = !isLoading,
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFFF05F22))
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                                } else {
                                    androidx.compose.material3.Text(
                                        "Registrarse", 
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, 
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
