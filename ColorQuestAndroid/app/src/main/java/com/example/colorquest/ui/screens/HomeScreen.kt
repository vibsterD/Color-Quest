package com.example.colorquest.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import java.io.File


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
            Text(text = "LESGOOOO" + capturedImageBitmap.width.toString() + capturedImageBitmap.height.toString())

        }

        capturedImageBitmap?.let { bitmap ->
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Captured Image!")
        }

        Button(onClick = {
                takePicture.launch(null)
        }) {
            Text(text = "Snap a pic!")
        }

        CaptureImageScreen()
    }

}


@Composable
fun CaptureImageScreen() {
    val context = LocalContext.current
    var currentCapturedImageUri by remember {
        mutableStateOf(Uri.EMPTY)
    }
    var tempImageUri by remember {
        mutableStateOf(Uri.EMPTY)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {success ->
            if(success) {
                // do something with the image
                currentCapturedImageUri = tempImageUri
                Log.d("[LOG]", "CameraLauncher: Success!")
            }else {
                currentCapturedImageUri = Uri.EMPTY
                Log.d("[LOG]", "CameraLauncher: Failure!")
            }
        })




    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick ={
            tempImageUri = context.createTempUri()
            Log.d("[LOG]", "Reached here" + tempImageUri.toString())
            cameraLauncher.launch(tempImageUri)
            Log.d("[LOG]", "Reached here too" + tempImageUri.toString())
        }) {
            Text(text = "Take Picture")
        }

        if(currentCapturedImageUri != Uri.EMPTY) {
            // display the image
            Text(text = "Image captured!" + currentCapturedImageUri.toString())
            val source = ImageDecoder.createSource(context.contentResolver, currentCapturedImageUri)
            val bitmap = ImageDecoder.decodeBitmap(source)
//            Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Captured Image!")

            AsyncImage(modifier = Modifier.size(200.dp),
                model = currentCapturedImageUri,
                contentDescription = "Captured Image!")
        }
    }
}





// https://stackoverflow.com/questions/75387353/activityresultcontracts-takepicture-it-is-always-returning-false-as-a-result/75391991#75391991
fun Context.createTempUri (): Uri {

    val directory = File(this.filesDir, "")
    if(!directory.exists()) {
        Log.d("[BUG]", "createTempUri: Directory doesn't exist")
        directory.mkdirs()
    }
    Log.d("[LOG]", packageName + ".provider")
    val fileName = "image_${System.currentTimeMillis()}.png"
    Log.d("[LOG]", fileName)

    val tempFile = File(directory, fileName).apply { createNewFile() }

    if(!tempFile.exists()) {
        Log.d("[BUG]", "onCreate: file couldn't be created")
        Log.d("[LOG]", tempFile.toString())
    }


    try {
        val uri =
            FileProvider.getUriForFile(applicationContext, packageName + ".provider", tempFile)
        Log.d("[LOG]", "SUCCESS!" + uri.encodedPath!!)
        return uri
    }catch (e: Exception) {
        Log.d("[BUG]", "createTempUri: ${e.message}")
        return Uri.EMPTY
    }

}