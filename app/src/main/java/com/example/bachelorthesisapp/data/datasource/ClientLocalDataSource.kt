package com.example.bachelorthesisapp.data.datasource

import com.example.bachelorthesisapp.data.dao.ActivitiesDao
import com.example.bachelorthesisapp.data.dao.BusinessesDao
import com.example.bachelorthesisapp.data.dao.ClientsDao
import com.example.bachelorthesisapp.data.model.entities.ActivityEntity
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.ClientEntity
import com.example.bachelorthesisapp.datasource.LocalDataSource
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