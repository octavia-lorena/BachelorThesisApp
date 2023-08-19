package com.example.bachelorthesisapp.data.model


abstract class OfferPostGeneric(
    id: Int,
    businessId: String,
    title: String,
    description: String,
    images: List<String>,
    price: Int,
    rating: Rating,
)
