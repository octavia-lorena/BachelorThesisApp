package com.example.bachelorthesisapp.data.appointment_requests.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bachelorthesisapp.data.BaseDao
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest

@Dao
abstract class AppointmentRequestDao : BaseDao<AppointmentRequest> {

    @Transaction
    open suspend fun repopulateRequests(requests: List<AppointmentRequest>): List<Long> {
        deleteAllRequests()
        return insertAll(requests)
    }

    @Query("SELECT * FROM requests")
    abstract suspend fun getAllRequests(): List<AppointmentRequest>

    @Query("SELECT * FROM requests WHERE `id` = :key LIMIT 1")
    abstract suspend fun getRequest(key: String): AppointmentRequest?

    @Query("DELETE FROM requests")
    abstract suspend fun deleteAllRequests()

    @Query("SELECT R.* FROM requests AS R INNER JOIN events AS E ON R.eventId = E.id WHERE E.organizerId = :clientId")
    abstract suspend fun getRequestByClientId(clientId: String): List<AppointmentRequest>

    @Query("SELECT R.* FROM requests AS R INNER JOIN posts AS P ON R.postId = P.id WHERE P.businessId = :businessId")
    abstract suspend fun getRequestByBusinessId(businessId: String): List<AppointmentRequest>

}