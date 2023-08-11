package com.example.bachelorthesisapp.data.events.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bachelorthesisapp.data.BaseDao
import com.example.bachelorthesisapp.data.events.local.entity.Event

@Dao
abstract class EventsDao : BaseDao<Event> {

    @Transaction
    open suspend fun repopulateEvents(posts: List<Event>): List<Long> {
        deleteAllEvents()
        return insertAll(posts)
    }

    @Query("SELECT * FROM events")
    abstract suspend fun getAllEvents(): List<Event>

    @Query("SELECT * FROM events WHERE `id` = :key LIMIT 1")
    abstract suspend fun getEvent(key: String): Event?

    @Query("DELETE FROM events")
    abstract suspend fun deleteAllEvents()


    @Query("SELECT * FROM events WHERE `organizerId` = :organizerId")
    abstract suspend fun getEventsByOrganizerId(organizerId: String): List<Event>

}