package com.example.bachelorthesisapp.data.appointment_requests.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.bachelorthesisapp.domain.model.RequestStatus

@Entity(tableName = "requests")
data class AppointmentRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val eventId: Int,
    val postId: Int,
    val status: RequestStatus
)
