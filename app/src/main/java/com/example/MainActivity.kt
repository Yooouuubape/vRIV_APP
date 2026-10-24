package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ui.VryvApp
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.data.VryvRepository.instance.initPreferences(this)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                VryvApp()
            }
        }
    }
}
