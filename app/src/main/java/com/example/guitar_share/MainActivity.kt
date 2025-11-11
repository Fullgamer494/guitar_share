package com.example.guitar_share

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.guitar_share.presentation.ui.navigation.NavManager
import com.example.guitar_share.presentation.ui.theme.Guitar_shareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Guitar_shareTheme {
                NavManager()
            }
        }
    }
}
