package com.example.bachelorthesisapp.data.remote

import com.example.bachelorthesisapp.data.model.entities.AppointmentRequest
import com.example.bachelorthesisapp.data.model.entities.RequestStatus

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

