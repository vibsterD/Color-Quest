package com.example.colorquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.colorquest.data.ImageDatabase
import com.example.colorquest.data.ImageEntity
import com.example.colorquest.data.ImageViewModel
import com.example.colorquest.ui.screens.SketchInterface
import com.example.colorquest.ui.theme.ColorQuestTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SketchInterfaceActivity : ComponentActivity() {
    private lateinit var imageViewModel: ImageViewModel
    private lateinit var appDatabase: ImageDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = ImageDatabase.getInstance(applicationContext)
        imageViewModel = ImageViewModel(appDatabase.imageDao())
        setContent {
            ColorQuestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SketchInterface(this@SketchInterfaceActivity, imageViewModel)
                }
            }
        }
    }
}