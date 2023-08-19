package com.example.bachelorthesisapp.data.events.remote

import android.util.Log
import com.example.bachelorthesisapp.data.model.toEventData
import com.example.bachelorthesisapp.data.notifications.NotificationApi
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.events.remote.api.EventApi
import com.example.bachelorthesisapp.data.events.remote.dto.EventDto
import javax.inject.Inject

class EventRemoteDataSourceImpl @Inject constructor(
    private val api: EventApi,
    private val notificationApi: NotificationApi,
) : EventRemoteDataSource {


    override suspend fun getAllEvents(): List<EventDto> = api.getEventsData()
    override suspend fun getEventById(eventId: Int): EventDto = api.getEventDataById(eventId)


    override suspend fun getEventByOrganizerId(organizerId: String): List<EventDto> =
        api.getEventDataByOrganizerId(organizerId)

    override suspend fun addEvent(event: Event): EventDto {
        Log.d("NEW_EVENT", "API: ${event.toEventData()}")
        return api.addEvent(event.toEventData())
    }

    override suspend fun deleteEvent(eventId: Int): EventDto = api.deleteEvent(eventId)
    override suspend fun setVendorForCategory(
        eventId: Int,
        category: String,
        postId: Int
    ): EventDto = api.setVendorForCategory(eventId, category, postId)

    override suspend fun setEventCost(eventId: Int, price: Int): EventDto =
        api.setEventCost(eventId, price)

    override suspend fun updateEvent(
        id: Int,
        name: String,
        description: String,
        date: String,
        time: String,
        guestNumber: Int,
        budget: Int
    ): EventDto = api.updateEvent(
        id = id,
        name = name,
        description = description,
        date = date,
        time = time,
        guestNumber = guestNumber,
        budget = budget
    )

    override suspend fun publishEvent(eventId: Int): EventDto = api.publishEvent(eventId)
}