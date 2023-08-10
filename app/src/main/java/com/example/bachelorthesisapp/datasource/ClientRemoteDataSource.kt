package com.example.bachelorthesisapp.datasource

import com.example.bachelorthesisapp.data.model.entities.AppointmentRequest
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.remote.AppointmentRequestDto
import com.example.bachelorthesisapp.data.remote.BusinessDto
import com.example.bachelorthesisapp.data.remote.ClientDto
import com.example.bachelorthesisapp.data.remote.EventDto
import com.example.bachelorthesisapp.data.remote.OfferPostDto

interface ClientRemoteDataSource {
    suspend fun getAllClients(): List<ClientDto>
    suspend fun getClientById(clientId: String): ClientDto
    suspend fun updateClientDeviceToken(id: String, token: String): ClientDto
}