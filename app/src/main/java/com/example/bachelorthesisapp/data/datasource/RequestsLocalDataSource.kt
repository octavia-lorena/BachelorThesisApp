package com.example.bachelorthesisapp.data.datasource

import com.example.bachelorthesisapp.data.dao.AppointmentRequestDao
import com.example.bachelorthesisapp.data.dao.EventsDao
import com.example.bachelorthesisapp.data.dao.OfferPostsDao
import com.example.bachelorthesisapp.data.model.entities.AppointmentRequest
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.datasource.LocalDataSource
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