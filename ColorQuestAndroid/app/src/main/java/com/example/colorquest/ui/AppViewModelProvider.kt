package com.example.colorquest.ui

import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.colorquest.ui.screens.HomeScreenViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        HomeScreenViewModel()
    }
}