package com.example.bachelorthesisapp.data.businesses.local

import com.example.bachelorthesisapp.data.LocalDataSource
import com.example.bachelorthesisapp.data.businesses.local.dao.BusinessesDao
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import javax.inject.Inject

class BusinessLocalDataSource @Inject constructor(
    private val businessesDao: BusinessesDao
) : LocalDataSource<String, BusinessEntity> {

    override suspend fun getAllEntities(): List<BusinessEntity> = businessesDao.getAllBusinesses()

    override suspend fun deleteAllEntities() = businessesDao.deleteAllBusinesses()

    override suspend fun repopulateEntities(entities: List<BusinessEntity>): List<Long> =
        businessesDao.repopulateBusinesses(entities)

    override suspend fun deleteEntity(entity: BusinessEntity) = businessesDao.delete(entity)

    override suspend fun updateEntity(entity: BusinessEntity) = businessesDao.update(entity)

    override suspend fun insertAll(entities: List<BusinessEntity>): List<Long> =
        businessesDao.insertAll(entities)

    override suspend fun insertEntity(entity: BusinessEntity): Long = businessesDao.insert(entity)

    override suspend fun getEntity(key: String): BusinessEntity? = businessesDao.getBusiness(key)

    suspend fun getBusinessesByType(type: String) = businessesDao.getBusinessesByType(type)

    suspend fun getBusinessesByCity(city: String) = businessesDao.getBusinessesByCity(city)


}