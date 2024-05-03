package com.example.colorquest.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.colorquest.ui.Screen


@Composable
fun HomeScreen(modifier: Modifier, navigateTo: (Screen) -> Unit) {


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navigateTo(Screen.CAMERA_CAPTURE) }) {
            Text("Capture Image")
        }
        Button(onClick = { navigateTo(Screen.SKETCH_INTERFACE)}) {
            Text("Sketch Interface")
        }
    }

}

