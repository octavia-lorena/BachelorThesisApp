package com.example.bachelorthesisapp.data.appointment_requests.remote.api

import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.appointment_requests.remote.dto.AppointmentRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RequestApi {

    @GET("/requests")
    suspend fun getAllRequests(): List<AppointmentRequestDto>

    @POST("/request")
    suspend fun addRequest(@Body appointmentRequest: AppointmentRequest): AppointmentRequestDto

    @GET("/requests/{businessId}")
    suspend fun getRequestsByBusinessId(
        @Path(
            value = "businessId",
            encoded = true
        )
        businessId: String
    ): List<AppointmentRequestDto>

    @DELETE("/request/{id}")
    suspend fun deleteRequest(
        @Path(
            value = "id",
            encoded = true
        )
        id: Int,
    ): AppointmentRequestDto

    @FormUrlEncoded
    @PUT("/request/{id}")
    suspend fun updateRequestStatus(
        @Path(
            value = "id",
            encoded = true
        )
        id: Int,
        @Field("status") status: String,
    ): AppointmentRequestDto

    @DELETE("/requests/{eventId}")
    suspend fun deleteRequestsByEventId(
        @Path(
            value = "eventId",
            encoded = true
        )
        eventId: Int
    ): List<AppointmentRequestDto>

    @DELETE("/appointment/{id}")
    suspend fun deleteAppointment(
        @Path(
            value = "id",
            encoded = true
        )
        id: Int,
    ): AppointmentRequestDto

}