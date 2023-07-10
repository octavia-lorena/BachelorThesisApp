package com.example.bachelorthesisapp.data.model.entities

import androidx.room.PrimaryKey


abstract class OfferPostGeneric(
    businessId: String,
    title: String,
    description: String,
    images: List<String>,
    price: Int,
    rating: Float,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
