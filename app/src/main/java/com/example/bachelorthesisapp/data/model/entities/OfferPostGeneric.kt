package com.example.bachelorthesisapp.data.model.entities

import android.net.Uri
import androidx.room.PrimaryKey


abstract class OfferPostGeneric(
    id: Int,
    businessId: String,
    title: String,
    description: String,
    images: List<Uri>,
    price: Int,
    rating: Double,
)
