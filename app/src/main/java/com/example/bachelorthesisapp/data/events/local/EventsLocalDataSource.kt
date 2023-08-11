package com.example.bachelorthesisapp.data.events.local

import com.example.bachelorthesisapp.data.LocalDataSource
import com.example.bachelorthesisapp.data.events.local.dao.EventsDao
import com.example.bachelorthesisapp.data.events.local.entity.Event
import javax.inject.Inject

class EventsLocalDataSource @Inject constructor(
    private val eventsDao: EventsDao
) : LocalDataSource<String, Event> {

    override suspend fun getAllEntities(): List<Event> = eventsDao.getAllEvents()

    override suspend fun deleteAllEntities() = eventsDao.deleteAllEvents()

    override suspend fun repopulateEntities(entities: List<Event>): List<Long> =
        eventsDao.repopulateEvents(entities)

    override suspend fun deleteEntity(entity: Event) = eventsDao.delete(entity)

    override suspend fun updateEntity(entity: Event) = eventsDao.update(entity)

    override suspend fun insertAll(entities: List<Event>): List<Long> =
        eventsDao.insertAll(entities)

    override suspend fun insertEntity(entity: Event): Long = eventsDao.insert(entity)

    override suspend fun getEntity(key: String): Event? = eventsDao.getEvent(key)

    suspend fun getEventsByOrganizerId(key: String): List<Event> =
        eventsDao.getEventsByOrganizerId(key)
}