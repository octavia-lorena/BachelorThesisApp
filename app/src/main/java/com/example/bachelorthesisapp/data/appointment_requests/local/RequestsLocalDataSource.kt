package com.example.bachelorthesisapp.data.appointment_requests.local

import com.example.bachelorthesisapp.data.LocalDataSource
import com.example.bachelorthesisapp.data.appointment_requests.local.dao.AppointmentRequestDao
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import javax.inject.Inject

class RequestsLocalDataSource @Inject constructor(
    private val appointmentRequestDao: AppointmentRequestDao
) : LocalDataSource<String, AppointmentRequest> {

    override suspend fun getAllEntities(): List<AppointmentRequest> =
        appointmentRequestDao.getAllRequests()

    override suspend fun deleteAllEntities() = appointmentRequestDao.deleteAllRequests()

    override suspend fun repopulateEntities(entities: List<AppointmentRequest>): List<Long> =
        appointmentRequestDao.repopulateRequests(entities)

    override suspend fun deleteEntity(entity: AppointmentRequest) =
        appointmentRequestDao.delete(entity)

    override suspend fun updateEntity(entity: AppointmentRequest) =
        appointmentRequestDao.update(entity)

    override suspend fun insertAll(entities: List<AppointmentRequest>): List<Long> =
        appointmentRequestDao.insertAll(entities)

    override suspend fun insertEntity(entity: AppointmentRequest): Long =
        appointmentRequestDao.insert(entity)

    override suspend fun getEntity(key: String): AppointmentRequest? =
        appointmentRequestDao.getRequest(key)

    suspend fun getRequestsByBusinessId(businessId: String): List<AppointmentRequest> = appointmentRequestDao.getRequestByBusinessId(businessId)
}