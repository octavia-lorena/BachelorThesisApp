package com.example.bachelorthesisapp.datasource

import com.example.bachelorthesisapp.data.model.entities.AppointmentRequest
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.remote.AppointmentRequestDto
import com.example.bachelorthesisapp.data.remote.BusinessDto
import com.example.bachelorthesisapp.data.remote.EventDto
import com.example.bachelorthesisapp.data.remote.OfferPostDto

interface RequestRemoteDataSource {
    suspend fun getAllRequests(): List<AppointmentRequestDto>
    suspend fun addRequest(appointmentRequest: AppointmentRequest): AppointmentRequestDto
    suspend fun getRequestsByBusinessId(businessId: String): List<AppointmentRequestDto>
    suspend fun deleteRequest(requestId: Int): AppointmentRequestDto
    suspend fun updateRequestStatus(id: Int, status: String): AppointmentRequestDto
    suspend fun deleteRequestsByEventId(eventId: Int): List<AppointmentRequestDto>
    suspend fun deleteAppointment(requestId: Int): AppointmentRequestDto
}