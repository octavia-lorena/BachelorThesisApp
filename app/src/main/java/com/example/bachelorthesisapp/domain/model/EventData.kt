package com.example.bachelorthesisapp.domain.model

import com.example.bachelorthesisapp.data.events.local.entity.Event
import java.time.LocalDate


data class EventData(
    val id: Int,
    val organizerId: String,
    val name: String,
    val description: String,
    val type: EventType,
    val date: LocalDate,
    val time: String,
    val guestNumber: Int,
    val budget: Int,
    val cost: Int,
    val vendors: Map<String, Int>,
    val status: EventStatus
)

fun Event.toEventData() = EventData(
    id = id,
    organizerId = organizerId,
    name = name,
    description = description,
    type = type,
    date = date,
    time = time,
    guestNumber = guestNumber,
    budget = budget,
    cost = cost,
    vendors = vendors.map { Pair(it.key.name, it.value) }.toMap(),
    status = status
)
