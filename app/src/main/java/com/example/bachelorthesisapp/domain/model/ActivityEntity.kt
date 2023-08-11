package com.example.bachelorthesisapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bachelorthesisapp.data.ActivityDto
import com.example.bachelorthesisapp.presentation.viewmodel.model.ActivityModel

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey val key: String,
    val activity: String,
    val type: String,
    val participants: Int,
    val price: Double,
    val link: String,
    val accessibility: Double
)

fun ActivityEntity.toModel(): ActivityModel = ActivityModel(
    name = activity,
    type = type,
    participants = participants,
    price = price,
    link = link,
    key = key,
    accessibility = accessibility
)

fun ActivityEntity.toDto(): ActivityDto = ActivityDto(
    name = activity,
    type = type,
    participants = participants,
    price = price,
    link = link,
    key = key,
    accessibility = accessibility
)
