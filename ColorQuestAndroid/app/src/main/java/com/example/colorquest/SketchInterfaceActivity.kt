package com.example.colorquest

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colorquest.data.ImageEntity
import com.example.colorquest.ui.AppViewModelProvider
import com.example.colorquest.ui.screens.HomeScreenViewModel
import com.example.colorquest.ui.screens.SketchInterface
import com.example.colorquest.ui.theme.ColorQuestTheme
import android.widget.Toast

class SketchInterfaceActivity : ComponentActivity(), ShakeDetector.OnShakeListener{


    private lateinit var shakeDetector: ShakeDetector

    private var imageUri: Uri = Uri.parse("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentThatStarted = intent
        val sketch: ImageEntity? = intentThatStarted.getParcelableExtra("sketch")


        shakeDetector = ShakeDetector(this)
        shakeDetector.setOnShakeListener(this)

        setContent {
            val homesScreenViewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)

            ColorQuestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    imageUri = SketchInterface(this@SketchInterfaceActivity, homesScreenViewModel, sketch, false, imageUri)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        shakeDetector.start()
    }

    override fun onPause() {
        super.onPause()
        shakeDetector.stop()
    }

    override fun onShake() {
        Toast.makeText(this, "Phone is being shaken!", Toast.LENGTH_SHORT).show()
        setContent {
            ColorQuestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SketchInterface(this@SketchInterfaceActivity,  viewModel(factory = AppViewModelProvider.Factory), null, true, imageUri)
                }
            }
        }
    }
}