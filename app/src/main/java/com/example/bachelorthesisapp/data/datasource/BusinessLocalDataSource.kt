package com.example.bachelorthesisapp.data.datasource

import com.example.bachelorthesisapp.data.dao.ActivitiesDao
import com.example.bachelorthesisapp.data.dao.BusinessesDao
import com.example.bachelorthesisapp.data.model.entities.ActivityEntity
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.datasource.LocalDataSource
import javax.inject.Inject

class BusinessLocalDataSource @Inject constructor(
    private val businessesDao: BusinessesDao
): LocalDataSource<String, BusinessEntity> {

    override suspend fun getAllEntities(): List<BusinessEntity> = businessesDao.getAllBusinesses()

    override suspend fun deleteAllEntities() = businessesDao.deleteAllBusinesses()

    override suspend fun repopulateEntities(entities: List<BusinessEntity>): List<Long> = businessesDao.repopulateBusinesses(entities)

    override suspend fun deleteEntity(entity: BusinessEntity) = businessesDao.delete(entity)

    override suspend fun updateEntity(entity: BusinessEntity) = businessesDao.update(entity)

    override suspend fun insertAll(entities: List<BusinessEntity>): List<Long> = businessesDao.insertAll(entities)

    override suspend fun insertEntity(entity: BusinessEntity): Long = businessesDao.insert(entity)

    override suspend fun getEntity(key: String): BusinessEntity? = businessesDao.getBusiness(key)

    suspend fun getBusinessesByType(type: String) = businessesDao.getBusinessesByType(type)


}