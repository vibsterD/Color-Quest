package com.example.colorquest.data

class OfflineImageRepository(private val imageDao: ImageDao): ImageRepository {


    override suspend fun insertImage(image: ImageEntity) {
        imageDao.insert(image)
    }

    override fun getAllImagesStream() = imageDao.getAllImages()


}