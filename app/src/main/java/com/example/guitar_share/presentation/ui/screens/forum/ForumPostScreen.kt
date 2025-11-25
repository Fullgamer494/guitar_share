package com.example.guitar_share.presentation.ui.screens.forum

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.guitar_share.presentation.ui.theme.EerielBlack
import com.example.guitar_share.presentation.ui.theme.OrangeAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumPostScreen(navController: NavController){
   var textInput by remember { mutableStateOf("") }
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Crear Publicación", textAlign = TextAlign.End, modifier = Modifier.fillMaxWidth())},
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
                value = textInput,
                onValueChange = {
                        newText -> textInput = newText
                },
                label = { Text("Título:")},
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                value = textInput,
                onValueChange = {
                        newText -> textInput = newText
                },
                label = { Text("Cuerpo de texto:")},
            )
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),
                value = textInput,
                onValueChange = {
                        newText -> textInput = newText
                },
                label = { Text("Añadir Imagen:")},
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeAction,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = { /*TODO*/ }
            ) {
                Text("Publicar")
            }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = EerielBlack,
                    contentColor = Color.White
                ),
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