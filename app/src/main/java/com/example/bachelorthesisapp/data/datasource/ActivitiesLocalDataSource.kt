package com.example.bachelorthesisapp.data.datasource

import com.example.bachelorthesisapp.data.dao.ActivitiesDao
import com.example.bachelorthesisapp.data.model.entities.ActivityEntity
import com.example.bachelorthesisapp.datasource.LocalDataSource
import javax.inject.Inject

class ActivitiesLocalDataSource @Inject constructor(
    private val activitiesDao: ActivitiesDao
): LocalDataSource<String, ActivityEntity> {

    override suspend fun getEntity(key: String): ActivityEntity? = activitiesDao.getActivity(key)

    override suspend fun getAllEntities(): List<ActivityEntity> = activitiesDao.getAllActivities()

    override suspend fun insertEntity(entity: ActivityEntity): Long = activitiesDao.insert(entity)

    override suspend fun insertAll(entities: List<ActivityEntity>): List<Long> =
        activitiesDao.insertAll(entities)

    override suspend fun updateEntity(entity: ActivityEntity) = activitiesDao.update(entity)

    override suspend fun deleteEntity(entity: ActivityEntity) = activitiesDao.delete(entity)

    override suspend fun deleteAllEntities() = activitiesDao.deleteAllActivities()
    override suspend fun repopulateEntities(entities: List<ActivityEntity>): List<Long> =
        activitiesDao.repopulateActivities(entities)
}