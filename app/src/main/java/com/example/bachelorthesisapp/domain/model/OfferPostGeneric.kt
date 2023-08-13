package com.example.bachelorthesisapp.domain.model

import android.net.Uri


abstract class OfferPostGeneric(
    id: Int,
    businessId: String,
    title: String,
    description: String,
    images: List<String>,
    price: Int,
    rating: Rating,
)
