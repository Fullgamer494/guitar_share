package com.example.guitar_share.presentation.ui.screens.scores_library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.guitar_share.presentation.ui.components.bottom_navbar.BottomNavBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewScore(navController: NavController){
    var scoreInput by remember { mutableStateOf("") }
    val EerielBlackForPlusButton = Color(0xFF1E1E1E)


    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Añadir Partitura", textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())},
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(navController) }
    ){ innerPadding ->

        Column (
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = scoreInput,
                onValueChange = {
                        newText -> scoreInput = newText
                },
                label = { Text("Título de la partitura:")},
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = scoreInput,
                onValueChange = {
                        newText -> scoreInput = newText
                },
                label = { Text("Artista:")},
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .weight(1f),
                    value = scoreInput,
                    onValueChange = {
                            newText -> scoreInput = newText
                    },
                    label = { Text("Género:")},
                )
                Spacer(modifier = Modifier.width(5.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .weight(1f),
                    value = scoreInput,
                    onValueChange = {
                            newText -> scoreInput = newText
                    },
                    label = { Text("Clave de afinación::")},
                )
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                value = scoreInput,
                onValueChange = {
                        newText -> scoreInput = newText
                },
                label = { Text("Añadir Imagen:")},
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .weight(1f),
                    value = scoreInput,
                    onValueChange = {
                            newText -> scoreInput = newText
                    },
                    label = { Text("Etiquetas:")},
                )
                Spacer(modifier = Modifier.width(5.dp))
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EerielBlackForPlusButton,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar"
                    )
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = { /*TODO*/ }
            ) {
                Text("Añadir Partitura")
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = { /*TODO*/ }
            ) {
                Text("Cancelar")
            }
        }
    }
}