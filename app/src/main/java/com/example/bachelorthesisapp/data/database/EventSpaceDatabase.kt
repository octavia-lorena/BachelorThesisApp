package com.example.bachelorthesisapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bachelorthesisapp.data.dao.ActivitiesDao
import com.example.bachelorthesisapp.data.model.entities.ActivityEntity

@Database(
    entities = [ActivityEntity::class],
    version = 2
)
abstract class EventSpaceDatabase: RoomDatabase() {
    companion object {
        const val databaseName = "eventspace_database"
    }

    abstract fun activitiesDao(): ActivitiesDao
}