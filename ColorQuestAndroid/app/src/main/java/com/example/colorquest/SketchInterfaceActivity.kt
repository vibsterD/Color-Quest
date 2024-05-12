package com.example.colorquest

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

class SketchInterfaceActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentThatStarted = intent
        val sketch: ImageEntity? = intentThatStarted.getParcelableExtra("sketch")

        setContent {
            val homesScreenViewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)

            ColorQuestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SketchInterface(this@SketchInterfaceActivity, homesScreenViewModel, sketch)
                }
            }
        }
    }
}