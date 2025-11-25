package com.example.guitar_share.presentation.ui.screens.scores_library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guitar_share.presentation.ui.components.bottom_navbar.BottomNavBar
import com.example.guitar_share.presentation.ui.theme.DarkCancel
import com.example.guitar_share.presentation.ui.theme.EerielBlack
import com.example.guitar_share.presentation.ui.theme.OrangeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewScore(navController: NavController){
    var titleInput by remember { mutableStateOf("") }
    var artistInput by remember { mutableStateOf("") }
    var genreInput by remember { mutableStateOf("") }
    var keyInput by remember { mutableStateOf("") }
    var imageInput by remember { mutableStateOf("") }
    var tagInput by remember { mutableStateOf("") }



    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Añadir Partitura",
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(navController) }
    ){ innerPadding ->

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ){
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Título de la partitura:", fontWeight = FontWeight.Medium)
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    placeholder = { Text("Ej: Lamento Boliviano") }
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Artista:", fontWeight = FontWeight.Medium)
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = artistInput,
                    onValueChange = { artistInput = it },
                    placeholder = { Text("Ej: Enanitos Verdes") }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Género:", fontWeight = FontWeight.Medium)
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = genreInput,
                        onValueChange = { genreInput = it },
                        placeholder = { Text("Rock") }
                    )
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Clave de afinación:", fontWeight = FontWeight.Medium)
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = keyInput,
                        onValueChange = { keyInput = it },
                        placeholder = { Text("E Standard") }
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Selecciona partitura (Imagen):", fontWeight = FontWeight.Medium)
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    value = imageInput,
                    onValueChange = { imageInput = it },
                    placeholder = {
                        Text("Pega el link de la imagen aquí...")
                    }
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Etiquetas:", fontWeight = FontWeight.Medium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        modifier = Modifier.weight(1f),
                        value = tagInput,
                        onValueChange = { tagInput = it },
                        placeholder = { Text("Ej: Acústico") }
                    )

                    Button(
                        onClick = { /* Acción agregar tag */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EerielBlack,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar etiqueta"
                        )
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
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeAction,
                        contentColor = Color.White
                    )
                ) {
                    Text("Añadir partitura", fontWeight = FontWeight.Bold)
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkCancel,
                        contentColor = Color.White
                    )
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}