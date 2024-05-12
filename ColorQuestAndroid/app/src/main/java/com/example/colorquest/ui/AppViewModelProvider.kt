package com.example.colorquest.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colorquest.ColorQuestApplication
import com.example.colorquest.ui.screens.HomeScreenViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeScreenViewModel(colorQuestApplication().container.imageRepository)
        }
    }
}


fun CreationExtras.colorQuestApplication(): ColorQuestApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ColorQuestApplication)