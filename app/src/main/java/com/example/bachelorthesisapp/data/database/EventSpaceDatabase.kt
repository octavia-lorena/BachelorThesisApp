package com.example.bachelorthesisapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bachelorthesisapp.data.dao.ActivitiesDao
import com.example.bachelorthesisapp.data.dao.AppointmentRequestDao
import com.example.bachelorthesisapp.data.dao.BusinessesDao
import com.example.bachelorthesisapp.data.dao.ClientsDao
import com.example.bachelorthesisapp.data.dao.EventsDao
import com.example.bachelorthesisapp.data.dao.OfferPostsDao
import com.example.bachelorthesisapp.data.model.entities.ActivityEntity
import com.example.bachelorthesisapp.data.model.entities.AppointmentRequest
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.ClientEntity
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.model.entities.PostComment
import com.google.android.gms.common.api.Api.Client

@Database(
    entities = [
        ActivityEntity::class,
        BusinessEntity::class,
        OfferPost::class,
        Event::class,
        AppointmentRequest::class,
        ClientEntity::class],
    version = 17
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