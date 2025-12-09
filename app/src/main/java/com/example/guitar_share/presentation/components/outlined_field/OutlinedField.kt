package com.example.guitar_share.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    // Puedes agregar más parámetros si los necesitas (ej: isError, trailingIcon)
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            // Texto
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,

            // Contenedor (Fondo)
            focusedContainerColor = Color.Transparent, // O MaterialTheme.colorScheme.surfaceVariant
            unfocusedContainerColor = Color.Transparent,

            // Bordes
            focusedBorderColor = MaterialTheme.colorScheme.primary, // Tu Naranja
            unfocusedBorderColor = MaterialTheme.colorScheme.outline, // Tu Gris definido en el tema

            // Etiquetas (Label)
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant, // Gris claro

            // Cursor
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}