package com.example.bachelorthesisapp.di

import android.content.Context
import androidx.room.Room
import com.example.bachelorthesisapp.data.dao.ActivitiesDao
import com.example.bachelorthesisapp.data.dao.AppointmentRequestDao
import com.example.bachelorthesisapp.data.dao.BusinessesDao
import com.example.bachelorthesisapp.data.dao.ClientsDao
import com.example.bachelorthesisapp.data.dao.EventsDao
import com.example.bachelorthesisapp.data.dao.OfferPostsDao
import com.example.bachelorthesisapp.data.database.EventSpaceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun providesProjectDatabase(
        @ApplicationContext applicationContext: Context
    ): EventSpaceDatabase = Room.databaseBuilder(
        context = applicationContext,
        klass = EventSpaceDatabase::class.java,
        name = EventSpaceDatabase.databaseName
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun providesActivitiesDao(database: EventSpaceDatabase): ActivitiesDao =
        database.activitiesDao()

    @Provides
    @Singleton
    fun providesBusinessDao(database: EventSpaceDatabase): BusinessesDao = database.businessDao()

    @Provides
    @Singleton
    fun providesPostDao(database: EventSpaceDatabase): OfferPostsDao = database.postsDao()

    @Provides
    @Singleton
    fun providesEventsDao(database: EventSpaceDatabase): EventsDao = database.eventsDao()

    @Provides
    @Singleton
    fun providesRequestsDao(database: EventSpaceDatabase): AppointmentRequestDao =
        database.requestsDao()

    @Provides
    @Singleton
    fun providesClientsDao(database: EventSpaceDatabase): ClientsDao = database.clientsDao()

}