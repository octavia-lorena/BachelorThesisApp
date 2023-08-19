package com.example.bachelorthesisapp.data.clients.remote

import com.example.bachelorthesisapp.data.clients.local.entity.ClientEntity
import com.example.bachelorthesisapp.data.clients.remote.dto.ClientDto

interface ClientRemoteDataSource {
    suspend fun getAllClients(): List<ClientDto>
    suspend fun getClientById(clientId: String): ClientDto
    suspend fun updateClientDeviceToken(id: String, token: String): ClientDto
    suspend fun addClient(client: ClientEntity): ClientDto
}