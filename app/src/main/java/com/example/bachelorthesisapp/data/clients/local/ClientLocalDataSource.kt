package com.example.bachelorthesisapp.data.clients.local

import com.example.bachelorthesisapp.data.LocalDataSource
import com.example.bachelorthesisapp.data.clients.local.dao.ClientsDao
import com.example.bachelorthesisapp.data.clients.local.entity.ClientEntity
import javax.inject.Inject

class ClientLocalDataSource @Inject constructor(
    private val clientsDao: ClientsDao
) : LocalDataSource<String, ClientEntity> {
    override suspend fun getAllEntities(): List<ClientEntity> = clientsDao.getAllClients()

    override suspend fun deleteAllEntities() = clientsDao.deleteAllClients()

    override suspend fun repopulateEntities(entities: List<ClientEntity>): List<Long> =
        clientsDao.repopulateClients(entities)

    override suspend fun deleteEntity(entity: ClientEntity) = clientsDao.delete(entity)

    override suspend fun updateEntity(entity: ClientEntity) = clientsDao.update(entity)

    override suspend fun insertAll(entities: List<ClientEntity>): List<Long> =
        clientsDao.insertAll(entities)

    override suspend fun insertEntity(entity: ClientEntity): Long = clientsDao.insert(entity)

    override suspend fun getEntity(key: String): ClientEntity? = clientsDao.getClient(key)

}