package com.example.bachelorthesisapp.data.businesses.remote.dto

import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.domain.model.BusinessType

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
    val phoneNumber: String,
    val profilePicture: String?,
    val deviceToken: String?
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
    phoneNumber = phoneNumber,
    profilePicture = profilePicture,
    deviceToken = deviceToken
)
