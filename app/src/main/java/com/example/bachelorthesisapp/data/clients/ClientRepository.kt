package com.example.bachelorthesisapp.data.clients

import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.core.remote.networkCall
import com.example.bachelorthesisapp.data.clients.local.entity.ClientEntity
import com.example.bachelorthesisapp.data.clients.local.ClientLocalDataSource
import com.example.bachelorthesisapp.data.clients.remote.ClientRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.clients.remote.dto.toEntity
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

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
                        Resource.Error(clients.cause)
                        val clientsLocal =
                            clientLocalDataSource.getAllEntities()
                        Resource.Success(clientsLocal)
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Resource.Success(clients.value.map { it.toEntity() })
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
                        Resource.Error(client.cause)
                        val clientLocal =
                            clientLocalDataSource.getEntity(clientId)
                        Resource.Success(clientLocal!!)
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Resource.Success(client.value.toEntity())
                    }
                }
            )
        })
}