package com.example.bachelorthesisapp.data.events.remote.api

import com.example.bachelorthesisapp.data.events.remote.dto.EventDto
import com.example.bachelorthesisapp.data.model.EventData
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EventApi {
    @GET("/events")
    suspend fun getEventsData(): List<EventDto>

    @GET("/events/{id}")
    suspend fun getEventDataByOrganizerId(
        @Path(
            value = "id",
            encoded = true
        ) organizerId: String
    ): List<EventDto>

    @GET("/event/{id}")
    suspend fun getEventDataById(
        @Path(
            value = "id",
            encoded = true
        ) eventId: Int
    ): EventDto

    @POST("/event")
    suspend fun addEvent(@Body event: EventData): EventDto

    @DELETE("/event/{id}")
    suspend fun deleteEvent(
        @Path(
            value = "id",
            encoded = true
        ) eventId: Int
    ): EventDto

    @FormUrlEncoded
    @PUT("/event/vendor/{id}")
    suspend fun setVendorForCategory(
        @Path(
            value = "id",
            encoded = true
        ) eventId: Int,
        @Field("category")
        category: String,
        @Field("postId")
        postId: Int
    ): EventDto

    @FormUrlEncoded
    @PUT("/event/cost/{id}")
    suspend fun setEventCost(
        @Path(
            value = "id",
            encoded = true
        ) eventId: Int,
        @Field("price")
        category: Int,
    ): EventDto

    @FormUrlEncoded
    @PUT("/event/{id}")
    suspend fun updateEvent(
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("date") date:String,
        @Field("time") time: String,
        @Field("guestNumber") guestNumber: Int,
        @Field("budget") budget: Int
    ): EventDto

    @PUT("/event/publish/{id}")
    suspend fun publishEvent(
        @Path(
            value = "id",
            encoded = true
        ) eventId: Int
    ): EventDto

    @PUT("/events/past")
    suspend fun setPastEvents(): List<EventDto>
}