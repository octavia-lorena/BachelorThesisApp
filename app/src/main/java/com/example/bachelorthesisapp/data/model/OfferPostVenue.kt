package com.example.bachelorthesisapp.data.model

import androidx.room.Entity

@Entity(tableName = "posts_venue")
class OfferPostVenue(
    val id: Int,
    val businessId: String,
    val title: String,
    val description: String,
    val images: List<String>,
    val price: Int,
    val rating: Rating,
    var maxCapacity: Int
) : OfferPostGeneric(
    id = id,
    businessId = businessId,
    title = title,
    description = description,
    images = images,
    price = price,
    rating = rating
)
