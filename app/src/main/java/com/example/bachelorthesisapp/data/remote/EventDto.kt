package com.example.bachelorthesisapp.data.remote

import android.net.Uri
import androidx.core.net.toUri
import com.example.bachelorthesisapp.data.model.entities.BusinessType
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.EventStatus
import com.example.bachelorthesisapp.data.model.entities.EventType
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import java.time.LocalDate

data class EventDto(
    val id: Int,
    val organizerId: String,
    val name: String,
    val description: String,
    val type: EventType,
    val date: String,
    val time: String,
    val guestNumber: Int,
    val budget: Int,
    val cost: Int,
    val vendors: Map<BusinessType, Int?>,
    val status: EventStatus
)

fun EventDto.toEntity(): Event = Event(
    id = id,
    organizerId = organizerId,
    name = name,
    description = description,
    type = type,
    date = LocalDate.parse(date),
    time = time,
    guestNumber = guestNumber,
    budget = budget,
    cost = cost,
    vendors = vendors,
    status = status
)
