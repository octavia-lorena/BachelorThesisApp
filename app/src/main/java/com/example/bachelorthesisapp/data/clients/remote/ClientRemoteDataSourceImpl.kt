package com.example.bachelorthesisapp.data.clients.remote

import com.example.bachelorthesisapp.data.clients.local.entity.ClientEntity
import com.example.bachelorthesisapp.data.clients.remote.api.ClientApi
import com.example.bachelorthesisapp.data.clients.remote.dto.ClientDto
import com.example.bachelorthesisapp.data.notifications.NotificationApi
import com.example.bachelorthesisapp.data.notifications.PushNotification
import javax.inject.Inject

class ClientRemoteDataSourceImpl @Inject constructor(
    private val api: ClientApi,
    private val notificationApi: NotificationApi,
) : ClientRemoteDataSource {


    suspend fun sendNotification(pushNotification: PushNotification) =
        notificationApi.postNotification(pushNotification)

    override suspend fun getAllClients(): List<ClientDto> = api.getAllClients()

    override suspend fun getClientById(clientId: String): ClientDto = api.getClientById(clientId)

    override suspend fun updateClientDeviceToken(id: String, token: String): ClientDto =
        api.updateClientDeviceToken(id, token)

    override suspend fun addClient(client: ClientEntity): ClientDto = api.addClient(client)
}