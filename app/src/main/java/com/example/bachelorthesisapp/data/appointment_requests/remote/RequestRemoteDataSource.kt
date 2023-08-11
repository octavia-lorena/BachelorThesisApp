package com.example.bachelorthesisapp.data.appointment_requests.remote

import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.appointment_requests.remote.dto.AppointmentRequestDto

interface RequestRemoteDataSource {
    suspend fun getAllRequests(): List<AppointmentRequestDto>
    suspend fun addRequest(appointmentRequest: AppointmentRequest): AppointmentRequestDto
    suspend fun getRequestsByBusinessId(businessId: String): List<AppointmentRequestDto>
    suspend fun deleteRequest(requestId: Int): AppointmentRequestDto
    suspend fun updateRequestStatus(id: Int, status: String): AppointmentRequestDto
    suspend fun deleteRequestsByEventId(eventId: Int): List<AppointmentRequestDto>
    suspend fun deleteAppointment(requestId: Int): AppointmentRequestDto
}