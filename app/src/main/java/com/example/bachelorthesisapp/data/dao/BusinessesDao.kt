package com.example.bachelorthesisapp.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity

@Dao
abstract class BusinessesDao: BaseDao<BusinessEntity> {

    @Transaction
    open suspend fun repopulateBusinesses(businesses: List<BusinessEntity>): List<Long> {
        deleteAllBusinesses()
        return insertAll(businesses)
    }

    @Query("SELECT * FROM businesses")
    abstract suspend fun getAllBusinesses(): List<BusinessEntity>

    @Query("SELECT * FROM businesses WHERE `id` = :key LIMIT 1")
    abstract suspend fun getBusiness(key: String): BusinessEntity?

    @Query("DELETE FROM businesses")
    abstract suspend fun deleteAllBusinesses()

    @Query("SELECT * FROM businesses WHERE `businessType` = :type")
    abstract suspend fun getBusinessesByType(type: String): List<BusinessEntity>

    @Query("SELECT * FROM businesses WHERE `city` = :city")
    abstract suspend fun getBusinessesByCity(city: String): List<BusinessEntity>
}