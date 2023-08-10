package com.example.bachelorthesisapp.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "requests")
data class AppointmentRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val eventId: Int,
    val postId: Int,
    val status: RequestStatus
)
