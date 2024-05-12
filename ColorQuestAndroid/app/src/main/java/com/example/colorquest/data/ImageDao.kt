package com.example.colorquest.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Insert
    suspend fun insert(image: ImageEntity)

    @Query("SELECT * FROM images")
    fun getAllImages(): Flow<List<ImageEntity>>
}
