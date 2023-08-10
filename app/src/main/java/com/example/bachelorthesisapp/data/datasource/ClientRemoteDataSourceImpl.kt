package com.example.bachelorthesisapp.data.datasource

import android.util.Log
import com.example.bachelorthesisapp.data.remote.BusinessApi
import com.example.bachelorthesisapp.data.remote.BusinessDto
import com.example.bachelorthesisapp.data.remote.ClientApi
import com.example.bachelorthesisapp.data.remote.ClientDto
import com.example.bachelorthesisapp.data.remote.NotificationApi
import com.example.bachelorthesisapp.data.repo.firebase.PushNotification
import com.example.bachelorthesisapp.datasource.ApiResponse
import com.example.bachelorthesisapp.datasource.BusinessRemoteDataSource
import com.example.bachelorthesisapp.datasource.ClientRemoteDataSource
import com.example.bachelorthesisapp.exception.NetworkCallException
import javax.inject.Inject
import retrofit2.Response

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
}