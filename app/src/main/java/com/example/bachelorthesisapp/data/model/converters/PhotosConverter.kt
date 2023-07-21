package com.example.bachelorthesisapp.data.model.converters

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter

class PhotosConverter {

    companion object {
        const val SEPARATOR = ","
    }

    @TypeConverter
    fun photosListToString(photosList: List<Uri>): String =
        photosList.joinToString(SEPARATOR)

    @TypeConverter
    fun stringToPhotosList(string: String): List<Uri> =
        string.split(SEPARATOR).map { it.toUri() }

    @TypeConverter
    fun uriToString(uri: Uri): String = uri.toString()

    @TypeConverter
    fun stringToUri(string: String): Uri = string.toUri()
}