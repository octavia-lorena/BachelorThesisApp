package com.example.bachelorthesisapp.data.events.remote

import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.events.remote.dto.EventDto

interface EventRemoteDataSource {
    suspend fun getAllEvents(): List<EventDto>
    suspend fun getEventById(eventId: Int): EventDto
    suspend fun getEventByOrganizerId(organizerId: String): List<EventDto>
    suspend fun addEvent(event: Event): EventDto
    suspend fun deleteEvent(eventId: Int): EventDto
    suspend fun setVendorForCategory(eventId: Int, category: String, postId: Int): EventDto
    suspend fun setEventCost(eventId: Int, price: Int): EventDto
    suspend fun updateEvent(
        id: Int,
        name: String,
        description: String,
        date: String,
        time: String,
        guestNumber: Int,
        budget: Int
    ): EventDto

    suspend fun publishEvent(eventId: Int): EventDto
}