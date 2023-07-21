package com.example.bachelorthesisapp.data.model.states

data class CreatePostFormState(
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val images: String = "",
    val imagesError: String? = null,
    val price: String = "",
    val priceError: String? = null,
    val rating: String = "",
    val ratingError: String? = null,
)