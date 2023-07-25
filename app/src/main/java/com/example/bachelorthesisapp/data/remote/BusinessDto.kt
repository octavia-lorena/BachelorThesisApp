package com.example.bachelorthesisapp.data.remote

import com.example.bachelorthesisapp.data.model.entities.ActivityEntity
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.BusinessType
import com.squareup.moshi.Json

data class BusinessDto(
    val id: String,
    val businessName: String,
    val username: String,
    val email: String,
    val password: String,
    val businessType: BusinessType,
    val city: String,
    val address: String,
    val lat: Double?,
    val lng: Double?,
    val phoneNumber: String
)

fun BusinessDto.toEntity(): BusinessEntity = BusinessEntity(
    id = id,
    businessName = businessName,
    username = username,
    email = email,
    password = password,
    businessType = businessType,
    city = city,
    address = address,
    lat = lat,
    lng = lng,
    phoneNumber = phoneNumber
)
