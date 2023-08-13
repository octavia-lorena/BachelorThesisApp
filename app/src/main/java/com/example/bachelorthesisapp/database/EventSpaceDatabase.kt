package com.example.bachelorthesisapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bachelorthesisapp.data.ActivitiesDao
import com.example.bachelorthesisapp.data.appointment_requests.local.dao.AppointmentRequestDao
import com.example.bachelorthesisapp.data.businesses.local.dao.BusinessesDao
import com.example.bachelorthesisapp.data.clients.local.dao.ClientsDao
import com.example.bachelorthesisapp.data.events.local.dao.EventsDao
import com.example.bachelorthesisapp.data.posts.local.dao.OfferPostsDao
import com.example.bachelorthesisapp.domain.model.ActivityEntity
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.data.clients.local.entity.ClientEntity
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost

@Database(
    entities = [
        ActivityEntity::class,
        BusinessEntity::class,
        OfferPost::class,
        Event::class,
        AppointmentRequest::class,
        ClientEntity::class],
    version = 18
)
abstract class EventSpaceDatabase : RoomDatabase() {
    companion object {
        const val databaseName = "eventspace_database"
    }

    abstract fun activitiesDao(): ActivitiesDao
    abstract fun businessDao(): BusinessesDao
    abstract fun postsDao(): OfferPostsDao
    abstract fun eventsDao(): EventsDao
    abstract fun requestsDao(): AppointmentRequestDao
    abstract fun clientsDao(): ClientsDao
}