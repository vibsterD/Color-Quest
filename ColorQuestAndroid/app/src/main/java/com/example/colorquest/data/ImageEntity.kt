package com.example.colorquest.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uri: String,
    val drawingName: String,
    val lastUpdatedTimestamp: Long,
    val serializedBrushPoints: String
)
