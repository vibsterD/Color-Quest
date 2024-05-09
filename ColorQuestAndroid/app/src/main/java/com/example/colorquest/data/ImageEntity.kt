package com.example.colorquest.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageData: ByteArray,
    val drawingName: String,
    val lastUpdatedTimestamp: Long
)
