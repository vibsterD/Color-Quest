package com.example.colorquest.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colorquest.data.ImageEntity
import com.example.colorquest.data.ImageRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


data class SavedSketchesUiState(val itemList: List<ImageEntity> = listOf())
class HomeScreenViewModel(private val imageRepository: ImageRepository): ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    var savedSketchesUiState: StateFlow<SavedSketchesUiState> =
        imageRepository
            .getAllImagesStream()
            .map { SavedSketchesUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = TIMEOUT_MILLIS),
                initialValue = SavedSketchesUiState()
            )


    suspend fun insertImage(image: ImageEntity) {
        imageRepository.insertImage(image)
    }

}