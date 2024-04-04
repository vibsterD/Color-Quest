package com.example.colorquest.ui.screens

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext


@Composable
fun HomeScreen(takePicture: ActivityResultLauncher<Void?>, capturedImageBitmap: Bitmap?, modifier: Modifier) {


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Color Compose!")

        
        if (capturedImageBitmap == null) {
            Text(text = "NULL")
        }else {
            Text(text = "LESGOOOO")
        }

        capturedImageBitmap?.let { bitmap ->
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Captured Image!")
        }

        Button(onClick = {
                takePicture.launch(null)
        }) {
            Text(text = "Snap a pic!")
        }

    }

}


