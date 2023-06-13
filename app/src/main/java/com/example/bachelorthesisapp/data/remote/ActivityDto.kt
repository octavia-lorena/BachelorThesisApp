package com.example.bachelorthesisapp.data.remote

import com.example.bachelorthesisapp.presentation.viewmodel.model.ActivityModel
import com.squareup.moshi.Json

data class ActivityDto(
    @Json(name = "activity")
    val name: String,
    val type: String,
    val participants: Int,
    val price: Double,
    val link: String,
    val key: String,
    val accessibility: Double
) {
    fun toActivityModel(): ActivityModel {
        return ActivityModel(
            name = name,
            type = type,
            participants = participants,
            price = price,
            link = link,
            key = key,
            accessibility = accessibility
        )
    }
}
