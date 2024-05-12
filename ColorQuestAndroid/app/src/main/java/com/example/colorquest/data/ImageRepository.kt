package com.example.colorquest.data

import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    suspend fun insertImage(image: ImageEntity)

    fun getAllImagesStream(): Flow<List<ImageEntity>>

}