package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ui.VryvApp
import com.example.ui.screens.splash.VryvSplashScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.data.VryvRepository.instance.initPreferences(this)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Заставка показывается один раз за запуск (не повторяется при повороте экрана).
                var showSplash by rememberSaveable { mutableStateOf(true) }
                Box(modifier = Modifier.fillMaxSize()) {
                    // Приложение рендерится под заставкой, чтобы после её растворения
                    // весь функционал был уже готов без «пустого» кадра.
                    VryvApp()
                    if (showSplash) {
                        VryvSplashScreen(onFinished = { showSplash = false })
                    }
                }
            }
        }
    }
}
