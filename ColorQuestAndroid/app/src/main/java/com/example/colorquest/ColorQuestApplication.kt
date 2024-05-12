package com.example.colorquest

import android.app.Application
import com.example.colorquest.data.AppContainer
import com.example.colorquest.data.AppDataContainer

class ColorQuestApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

}

