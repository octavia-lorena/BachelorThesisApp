package com.example.bachelorthesisapp.data.posts.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.bachelorthesisapp.data.model.converters.PhotosConverter
import com.example.bachelorthesisapp.data.model.OfferPostGeneric
import com.example.bachelorthesisapp.data.model.Rating

@Entity(tableName = "posts")
@TypeConverters(PhotosConverter::class)
class OfferPost(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val businessId: String,
    val title: String,
    val description: String,
    val images: List<String>,
    val price: Int,
    val rating: Rating,
) : OfferPostGeneric(
    id = id,
    businessId = businessId,
    title = title,
    description = description,
    images = images,
    price = price,
    rating = rating
)
