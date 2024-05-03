package com.example.colorquest.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.colorquest.ui.screens.CaptureImageScreen
import com.example.colorquest.ui.screens.HomeScreen
import com.example.colorquest.ui.screens.SketchInterfaceScreen


// enum for the different screens
enum class Screen {
    HOME,
    SKETCH_INTERFACE,
    CAMERA_CAPTURE,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorQuestApp() {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }


    Scaffold(
        topBar = { TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = { Text(text = "Color Quest") })}
    ) {innerPadding ->
        Surface(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
            if (currentScreen == Screen.HOME) {
                HomeScreen(Modifier.fillMaxSize(), { screen ->  currentScreen = screen})
            } else if (currentScreen == Screen.SKETCH_INTERFACE) {
                 SketchInterfaceScreen()
            } else if (currentScreen == Screen.CAMERA_CAPTURE) {
                 CaptureImageScreen()
            }

        }
    }
}