package com.example.bachelorthesisapp.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bachelorthesisapp.data.BaseDao
import com.example.bachelorthesisapp.domain.model.ActivityEntity

@Dao
abstract class ActivitiesDao: BaseDao<ActivityEntity> {

    @Transaction
    open suspend fun repopulateActivities(activities: List<ActivityEntity>): List<Long> {
       deleteAllActivities()
        return insertAll(activities)
    }

    @Query("SELECT * FROM activities")
    abstract suspend fun getAllActivities(): List<ActivityEntity>

    @Query("SELECT * FROM activities WHERE `key` = :key LIMIT 1")
    abstract suspend fun getActivity(key: String): ActivityEntity?

    @Query("DELETE FROM activities")
    abstract suspend fun deleteAllActivities()
}