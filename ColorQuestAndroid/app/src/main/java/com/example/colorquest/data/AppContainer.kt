package com.example.colorquest.data

import android.content.Context

interface AppContainer {
    val imageRepository: ImageRepository
}

// This will be the source of all data objects which need to follow singleton
// pattern such as repositories
class AppDataContainer(private val context: Context) :AppContainer {
    override val imageRepository: ImageRepository by lazy {
        OfflineImageRepository(ImageDatabase.getInstance(context).imageDao())
    }

}