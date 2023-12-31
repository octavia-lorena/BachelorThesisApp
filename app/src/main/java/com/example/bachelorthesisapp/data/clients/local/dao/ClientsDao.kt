package com.example.bachelorthesisapp.data.clients.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bachelorthesisapp.data.BaseDao
import com.example.bachelorthesisapp.data.clients.local.entity.ClientEntity

@Dao
abstract class ClientsDao : BaseDao<ClientEntity> {

    @Transaction
    open suspend fun repopulateClients(clients: List<ClientEntity>): List<Long> {
        deleteAllClients()
        return insertAll(clients)
    }

    @Query("SELECT * FROM clients")
    abstract suspend fun getAllClients(): List<ClientEntity>

    @Query("SELECT * FROM clients WHERE `id` = :key LIMIT 1")
    abstract suspend fun getClient(key: String): ClientEntity?

    @Query("DELETE FROM clients")
    abstract suspend fun deleteAllClients()

}