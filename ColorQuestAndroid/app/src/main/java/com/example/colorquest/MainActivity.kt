package com.example.colorquest

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.colorquest.ui.ColorQuestApp
import com.example.colorquest.ui.theme.ColorQuestTheme


class MainActivity : ComponentActivity() {

    var capturedImageBitmap: Bitmap? by mutableStateOf(null)

    val takePicturePreview = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { imageBitmap ->

        if (imageBitmap != null) {
            // success
            capturedImageBitmap = imageBitmap
        }else {
            capturedImageBitmap = null
        }

    }



//    Uri uri = FileProvider.getUriForFile(this, "com.example.colorquest.fileprovider", file)


    val takePicture = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ){success ->
        if(success) {
//            capturedImageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, file.toUri())
//            ImageDecoder.Source source = ImageDecoder.createSource(this.contentResolver, )
        }else {
            capturedImageBitmap = null
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        applicationContext.createTempUri()

        setContent {
            ColorQuestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ColorQuestApp(takePicturePreview, capturedImageBitmap)
//                    Greeting("Android")
                }
            }
        }

    }

}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ColorQuestTheme {
        Greeting("Android")
    }
}