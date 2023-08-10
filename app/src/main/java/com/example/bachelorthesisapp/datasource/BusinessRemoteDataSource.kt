package com.example.bachelorthesisapp.datasource

import com.example.bachelorthesisapp.data.model.entities.AppointmentRequest
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.remote.AppointmentRequestDto
import com.example.bachelorthesisapp.data.remote.BusinessDto
import com.example.bachelorthesisapp.data.remote.EventDto
import com.example.bachelorthesisapp.data.remote.OfferPostDto

interface BusinessRemoteDataSource {
    suspend fun getAllBusiness(): List<BusinessDto>
    suspend fun getBusinessByType(type: String): List<BusinessDto>
    suspend fun getBusinessByCity(city: String): List<BusinessDto>
    suspend fun getBusinessById(businessId: String): BusinessDto
    suspend fun updateBusinessDeviceToken(id: String, token: String): BusinessDto
}