package com.example.bachelorthesisapp.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts_venue")
class OfferPostVenue(
    val businessId: String,
    val title: String,
    val description: String,
    val images: List<String>,
    val price: Int,
    val rating: Float,
    var maxCapacity: Int
) : OfferPostGeneric(
    businessId = businessId,
    title = title,
    description = description,
    images = images,
    price = price,
    rating = rating
)
