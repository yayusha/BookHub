package com.example.sem3project.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.sem3project.ui.theme.Sem3ProjectTheme
import com.example.sem3project.utils.ThemePreferences

class MainNavActivity : ComponentActivity() {
    private lateinit var themePreferences: ThemePreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        themePreferences = ThemePreferences(this)
        
        enableEdgeToEdge()
        setContent {
            // Load saved theme preference
            var isDarkMode by remember { mutableStateOf(themePreferences.isDarkMode()) }

            Sem3ProjectTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNav(
                        isDarkMode = isDarkMode,
                        onThemeChange = { newMode ->
                            isDarkMode = newMode
                            themePreferences.setDarkMode(newMode)
                        }
                    )
                }
            }
        }
    }
}