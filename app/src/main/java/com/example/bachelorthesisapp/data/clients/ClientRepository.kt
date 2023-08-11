package com.example.bachelorthesisapp.data.clients

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.bachelorthesisapp.data.businesses.local.BusinessLocalDataSource
import com.example.bachelorthesisapp.data.businesses.remote.BusinessRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.events.remote.EventRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.events.local.EventsLocalDataSource
import com.example.bachelorthesisapp.data.posts.remote.PostRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.posts.local.PostsLocalDataSource
import com.example.bachelorthesisapp.data.appointment_requests.remote.RequestRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.appointment_requests.local.RequestsLocalDataSource
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.domain.model.EventStatus
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.domain.model.RequestStatus
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.core.remote.networkCall
import com.example.bachelorthesisapp.data.businesses.remote.dto.toEntity
import com.example.bachelorthesisapp.data.notifications.PushNotification
import com.example.bachelorthesisapp.core.remote.NetworkCallException
import com.example.bachelorthesisapp.data.appointment_requests.remote.dto.toEntity
import com.example.bachelorthesisapp.data.clients.local.entity.ClientEntity
import com.example.bachelorthesisapp.data.clients.local.ClientLocalDataSource
import com.example.bachelorthesisapp.data.clients.remote.ClientRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.clients.remote.dto.toEntity
import com.example.bachelorthesisapp.data.events.remote.dto.toEntity
import com.example.bachelorthesisapp.data.posts.remote.dto.toEntity
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

class ClientRepository @Inject constructor(
    private val clientLocalDataSource: ClientLocalDataSource,
    private val clientRemoteDataSource: ClientRemoteDataSourceImpl

) {
    // CLIENT BY ID FLOW
    private val _clientFlow = MutableSharedFlow<Resource<ClientEntity>>()
    val clientFlow: Flow<Resource<ClientEntity>> = _clientFlow

    // ALL CLIENTS FLOW
    private val _clientsFlow = MutableSharedFlow<Resource<List<ClientEntity>>>()
    val clientsFlow: Flow<Resource<List<ClientEntity>>> = _clientsFlow


    suspend fun fetchClients() = networkCall(
        localSource = { clientLocalDataSource.getAllEntities() },
        remoteSource = { clientRemoteDataSource.getAllClients() },
        compareData = { remoteBusiness, localBusiness ->
            if (remoteBusiness != localBusiness) {
                val businessEntities = remoteBusiness.map { it.toEntity() }
                clientLocalDataSource.repopulateEntities(businessEntities)
            }
        },
        onResult = { clients ->
            _clientsFlow.emit(
                when (clients) {
                    is Resource.Error -> {
                        Resource.Error<Exception>(clients.exception)
                        val clientsLocal =
                            clientLocalDataSource.getAllEntities()
                        Resource.Success(clientsLocal)
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Resource.Success(clients.data.map { it.toEntity() })
                    }
                }
            )
        }
    )

    suspend fun fetchClientById(clientId: String) = networkCall(
        localSource = {
            clientLocalDataSource.getEntity(clientId)
        },
        remoteSource = {
            clientRemoteDataSource.getClientById(clientId)
        },
        compareData = { _, _ ->
            fetchClients()
            delay(2000L)
        },
        onResult = { client ->
            _clientFlow.emit(
                when (client) {
                    is Resource.Error -> {
                        Resource.Error<Exception>(client.exception)
                        val clientLocal =
                            clientLocalDataSource.getEntity(clientId)
                        Resource.Success(clientLocal!!)
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Resource.Success(client.data.toEntity())
                    }
                }
            )
        })
}