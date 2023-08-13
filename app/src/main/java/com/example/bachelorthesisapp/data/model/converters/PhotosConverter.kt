package com.example.bachelorthesisapp.data.model.converters

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter
import com.example.bachelorthesisapp.domain.model.Rating

class PhotosConverter {

    companion object {
        const val SEPARATOR = ","
    }

    @TypeConverter
    fun photosListToString(photosList: List<String>): String =
        photosList.joinToString(SEPARATOR)

    @TypeConverter
    fun stringToPhotosList(string: String): List<String> =
        string.split(SEPARATOR)

    @TypeConverter
    fun uriToString(uri: Uri): String = uri.toString()

    @TypeConverter
    fun stringToUri(string: String): Uri = string.toUri()

    @TypeConverter
    fun ratingToString(rating: Rating) = "${rating.value};${rating.voterCount}"

    @TypeConverter
    fun stringToRating(string: String): Rating {
        val elements = string.split(";")
        return Rating(
            value = elements[0].toDouble(),
            voterCount = elements[1].toInt()
        )
    }
}