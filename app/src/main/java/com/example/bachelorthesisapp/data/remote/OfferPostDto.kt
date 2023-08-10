package com.example.bachelorthesisapp.data.remote

import android.net.Uri
import androidx.core.net.toUri
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.model.entities.Rating

data class OfferPostDto(
    val id: Int,
    val businessId: String,
    val title: String,
    val description: String,
    val images: List<String>,
    val price: Int,
    val rating: Map<String, String>
)

fun OfferPostDto.toEntity(): OfferPost = OfferPost(
    id = id,
    businessId = businessId,
    title = title,
    description = description,
    images = images.map { it.toUri() },
    price = price,
    rating = Rating(
        value = rating.values.toList()[0].toDouble(),
        voterCount = rating.values.toList()[1].toInt()
    )
)
