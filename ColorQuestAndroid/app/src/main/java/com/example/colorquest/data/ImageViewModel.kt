package com.example.colorquest.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ImageViewModel(private val imageDao: ImageDao) : ViewModel() {

    fun insert(orientation: ImageEntity) = viewModelScope.launch {
        imageDao.insert(orientation)
    }

    suspend fun getAllImages() =
        imageDao.getAllImages()
}