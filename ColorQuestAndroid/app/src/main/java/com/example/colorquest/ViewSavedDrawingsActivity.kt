package com.example.colorquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.colorquest.data.ImageDatabase
import com.example.colorquest.data.ImageViewModel
import com.example.colorquest.ui.ColourQuestApp
import com.example.colorquest.ui.screens.ViewSavedDrawings
import com.example.colorquest.ui.theme.ColorQuestTheme

class ViewSavedDrawingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColorQuestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ViewSavedDrawings()
                }
            }
        }

    }
}