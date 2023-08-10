package com.example.bachelorthesisapp.datasource

import com.example.bachelorthesisapp.data.model.entities.AppointmentRequest
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.remote.AppointmentRequestDto
import com.example.bachelorthesisapp.data.remote.BusinessDto
import com.example.bachelorthesisapp.data.remote.EventDto
import com.example.bachelorthesisapp.data.remote.OfferPostDto

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