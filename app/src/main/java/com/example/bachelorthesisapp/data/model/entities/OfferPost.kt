package com.example.bachelorthesisapp.data.model.entities

import androidx.room.Entity

@Entity(tableName = "posts")
class OfferPost(
    val businessId: String,
    val title: String,
    val description: String,
    val images: List<String>,
    val price: Int,
    val rating: Float,
) : OfferPostGeneric(
    businessId = businessId,
    title = title,
    description = description,
    images = images,
    price = price,
    rating = rating
)
