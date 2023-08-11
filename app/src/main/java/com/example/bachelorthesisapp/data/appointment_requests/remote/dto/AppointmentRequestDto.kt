package com.example.bachelorthesisapp.data.appointment_requests.remote.dto

import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.domain.model.RequestStatus

data class AppointmentRequestDto(
    val id: Int,
    val eventId: Int,
    val postId: Int,
    val status: String
)

fun AppointmentRequestDto.toEntity(): AppointmentRequest {
    val statusList = enumValues<RequestStatus>()
    val statusValue = statusList.first { it.name == status }
    return AppointmentRequest(
        id = id,
        eventId = eventId,
        postId = postId,
        status = statusValue
    )
}

