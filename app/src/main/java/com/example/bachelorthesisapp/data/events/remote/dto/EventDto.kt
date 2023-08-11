package com.example.bachelorthesisapp.data.events.remote.dto

import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.domain.model.BusinessType
import com.example.bachelorthesisapp.domain.model.EventStatus
import com.example.bachelorthesisapp.domain.model.EventType
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
    val vendors: Map<String, Int>,
    val status: EventStatus
)

fun EventDto.toEntity(): Event {
    val types = enumValues<BusinessType>()
    return Event(
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
        vendors = vendors.map { vendor ->
            Pair(
                types.first { it.name == vendor.key },
                vendor.value
            )
        }.toMap(),
        status = status
    )

}
