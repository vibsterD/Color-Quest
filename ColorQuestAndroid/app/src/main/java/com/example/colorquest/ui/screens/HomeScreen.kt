package com.example.colorquest.ui.screens

import android.Manifest
import android.content.ContentResolver
import android.net.Uri
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.colorquest.ui.Screen
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun HomeScreen(modifier: Modifier, navigateTo: (Screen) -> Unit) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            navigateTo(Screen.CAMERA_CAPTURE)  }) {
            Text("Capture Image")
        }
        Button(onClick = { navigateTo(Screen.SKETCH_INTERFACE)}) {
            Text("Sketch Interface")
        }
    }



}


