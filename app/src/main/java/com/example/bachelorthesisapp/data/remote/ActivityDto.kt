package com.example.bachelorthesisapp.data.remote

import com.example.bachelorthesisapp.data.model.entities.ActivityEntity
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
)
fun ActivityDto.toEntity(): ActivityEntity = ActivityEntity(
    key = key,
    activity = name,
    type = type,
    participants = participants,
    price = price,
    link = link,
    accessibility = accessibility
)
