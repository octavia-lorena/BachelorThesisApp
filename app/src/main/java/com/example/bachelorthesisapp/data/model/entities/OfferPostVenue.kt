package com.example.bachelorthesisapp.data.model.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts_venue")
class OfferPostVenue(
    val id: Int,
    val businessId: String,
    val title: String,
    val description: String,
    val images: List<Uri>,
    val price: Int,
    val rating: Double,
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
