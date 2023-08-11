package com.example.bachelorthesisapp.data.businesses.remote

import com.example.bachelorthesisapp.data.businesses.remote.dto.BusinessDto

interface BusinessRemoteDataSource {
    suspend fun getAllBusiness(): List<BusinessDto>
    suspend fun getBusinessByType(type: String): List<BusinessDto>
    suspend fun getBusinessByCity(city: String): List<BusinessDto>
    suspend fun getBusinessById(businessId: String): BusinessDto
    suspend fun updateBusinessDeviceToken(id: String, token: String): BusinessDto
}