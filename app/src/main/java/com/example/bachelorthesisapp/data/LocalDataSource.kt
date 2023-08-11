package com.example.bachelorthesisapp.data

/**
 * Local data source for K (primary key), T (type) that contains basic operations, where K is the key type
 * of the entity and T is the class of the data stored.
 */
interface LocalDataSource<K, T> {

    suspend fun getAllEntities(): List<T>

    suspend fun getEntity(key: K): T?

    suspend fun insertEntity(entity: T): Long

    suspend fun insertAll(entities: List<T>): List<Long>

    suspend fun updateEntity(entity: T)

    suspend fun deleteEntity(entity: T)

    suspend fun deleteAllEntities()

    suspend fun repopulateEntities(entities: List<T>): List<Long>
}
