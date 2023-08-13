package com.example.bachelorthesisapp.domain.model

import android.net.Uri
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost

data class OfferPostModel(
    val id: Int,
    val businessId: String,
    val title: String,
    val description: String,
    val images: List<String>,
    val price: Int,
    val rating: Map<String, String>,
)

fun OfferPost.toModel() = OfferPostModel(
    id = id,
    businessId = businessId,
    title = title,
    description = description,
    images = images,
    price = price,
    rating = mapOf(
        Pair("value", rating.value.toString()),
        Pair("voterCount", rating.voterCount.toString())
    )
)
