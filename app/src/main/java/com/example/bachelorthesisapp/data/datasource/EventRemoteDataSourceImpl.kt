package com.example.bachelorthesisapp.data.datasource

import android.util.Log
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.toEventData
import com.example.bachelorthesisapp.data.remote.EventApi
import com.example.bachelorthesisapp.data.remote.EventDto
import com.example.bachelorthesisapp.data.remote.NotificationApi
import com.example.bachelorthesisapp.datasource.ApiResponse
import com.example.bachelorthesisapp.datasource.EventRemoteDataSource
import com.example.bachelorthesisapp.exception.NetworkCallException
import javax.inject.Inject
import retrofit2.Response

class EventRemoteDataSourceImpl @Inject constructor(
    private val api: EventApi,
    private val notificationApi: NotificationApi,
) : EventRemoteDataSource {

    private suspend fun <T> handleApiResponse(execute: suspend () -> Response<T>): ApiResponse<T> =
        try {
            val response = execute()
            if (response.isSuccessful) {
                response.body()?.let {
                    ApiResponse.Success(it)
                } ?: ApiResponse.SuccessWithoutBody
            } else {
                ApiResponse.Error(NetworkCallException(response.code().toString()))
            }
        } catch (ex: Exception) {
            Log.e(EventRemoteDataSourceImpl::class.simpleName, ex.stackTraceToString())
            ApiResponse.Error(exception = ex)
        }


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