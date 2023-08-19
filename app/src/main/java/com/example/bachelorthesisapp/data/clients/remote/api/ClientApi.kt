package com.example.bachelorthesisapp.data.clients.remote.api

import com.example.bachelorthesisapp.data.clients.local.entity.ClientEntity
import com.example.bachelorthesisapp.data.clients.remote.dto.ClientDto
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ClientApi {

    @GET("/clients")
    suspend fun getAllClients(): List<ClientDto>


    @GET("/client/{id}")
    suspend fun getClientById(
        @Path(
            value = "id",
            encoded = true
        ) clientId: String
    ): ClientDto

    @FormUrlEncoded
    @PUT("/client/{id}")
    suspend fun updateClientDeviceToken(
        @Path("id") id: String,
        @Field("token") token: String,
    ): ClientDto

    @POST("/client")
    suspend fun addClient(@Body client: ClientEntity): ClientDto
}