package com.example.bachelorthesisapp.data.appointment_requests

import android.util.Log
import com.example.bachelorthesisapp.core.remote.networkCall
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.core.resources.toResource
import com.example.bachelorthesisapp.data.appointment_requests.local.RequestsLocalDataSource
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.appointment_requests.remote.RequestRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.appointment_requests.remote.dto.AppointmentRequestDto
import com.example.bachelorthesisapp.data.appointment_requests.remote.dto.toEntity
import com.example.bachelorthesisapp.data.model.RequestStatus
import com.example.bachelorthesisapp.data.posts.remote.dto.OfferPostDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class AppointmentRequestsRepository @Inject constructor(
    private val requestsLocalDataSource: RequestsLocalDataSource,
    private val requestRemoteDataSource: RequestRemoteDataSourceImpl,
) {

    // REQUESTS FLOW (REQUEST STATUS = PENDING)
    private val _requestFlow = MutableSharedFlow<Resource<List<AppointmentRequest>>>()
    val requestFlow: Flow<Resource<List<AppointmentRequest>>> = _requestFlow

    // REQUESTS FLOW (REQUEST STATUS = ACCEPTED)
    private val _appointmentFlow = MutableSharedFlow<Resource<List<AppointmentRequest>>>()
    val appointmentFlow: Flow<Resource<List<AppointmentRequest>>> = _appointmentFlow

    // REQUEST RESULT FLOW
    private val _requestResultFlow = MutableSharedFlow<Resource<AppointmentRequest>>()
    val requestResultFlow: Flow<Resource<AppointmentRequest>> = _requestResultFlow
    private suspend fun fetchAllRequests() = networkCall(
        localSource = { requestsLocalDataSource.getAllEntities() },
        remoteSource = { requestRemoteDataSource.getAllRequests() },
        compareData = { remoteRequests, localRequests ->
            if (remoteRequests != localRequests) {
                Log.d("REQUESTS", "data updates")
                val requestsEntities = remoteRequests.map { it.toEntity() }
                requestsLocalDataSource.repopulateEntities(requestsEntities)
            }
        },
        onResult = {
        }
    )

//    suspend fun fetchRequestsByBusinessId(businessId: String) = networkCall(
//        localSource = { requestsLocalDataSource.getRequestsByBusinessId(businessId = businessId) },
//        remoteSource = { requestRemoteDataSource.getRequestsByBusinessId(businessId) },
//        compareData = { _, _ ->
//            fetchAllRequests()
//        },
//        onResult = { requests ->
//            _requestFlow.emit(
//                when (requests) {
//                    is Resource.Success -> {
//                        Resource.Success(requests.value.map { it.toEntity() }
//                            .filter { it.status == RequestStatus.Pending })
//                    }
//
//                    is Resource.Loading -> {
//                        Resource.Loading()
//                    }
//
//                    is Resource.Error -> {
//                        Resource.Error(requests.cause)
//                        val requestsLocal =
//                            requestsLocalDataSource.getRequestsByBusinessId(businessId = businessId)
//                        Resource.Success(requestsLocal.filter { it.status == RequestStatus.Pending })
//                    }
//                }
//            )
//            _appointmentFlow.emit(
//                when (requests) {
//                    is Resource.Success -> {
//                        Resource.Success(requests.value.map { it.toEntity() }
//                            .filter { it.status == RequestStatus.Accepted })
//                    }
//
//                    is Resource.Loading -> {
//                        Resource.Loading()
//                    }
//
//                    is Resource.Error -> {
//                        Resource.Error(requests.cause)
//                        val requestsLocal =
//                            requestsLocalDataSource.getRequestsByBusinessId(businessId = businessId)
//                        Resource.Success(requestsLocal.filter { it.status == RequestStatus.Accepted })
//                    }
//                }
//            )
//        }
//    )

//    suspend fun fetchAppointmentsByBusinessId(businessId: String) = networkCall(
//        localSource = { requestsLocalDataSource.getRequestsByBusinessId(businessId = businessId) },
//        remoteSource = { requestRemoteDataSource.getRequestsByBusinessId(businessId) },
//        compareData = { _, _ ->
//            fetchAllRequests()
//        },
//        onResult = { requests ->
//            _appointmentFlow.emit(
//                when (requests) {
//                    is Resource.Success -> {
//                        Resource.Success(requests.value.map { it.toEntity() }
//                            .filter { it.status == RequestStatus.Accepted })
//                    }
//
//                    is Resource.Loading -> {
//                        Resource.Loading()
//                    }
//
//                    is Resource.Error -> {
//                        Resource.Error(requests.cause)
//                        val requestsLocal =
//                            requestsLocalDataSource.getRequestsByBusinessId(businessId = businessId)
//                        Resource.Success(requestsLocal.filter { it.status == RequestStatus.Accepted })
//                    }
//                }
//            )
//        }
//    )

    suspend fun getRequestsByBusinessId(businessId: String): Resource<List<AppointmentRequestDto>> {
        return requestRemoteDataSource.getRequestsByBusinessId(businessId).toResource()
    }

    suspend fun createRequest(appointmentRequest: AppointmentRequest) {
        try {
            _requestResultFlow.emit(Resource.Loading())
            val result = requestRemoteDataSource.addRequest(appointmentRequest)
            _requestResultFlow.emit(Resource.Success(result.toEntity()))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CREATE_REQUEST", e.stackTraceToString())
            _requestResultFlow.emit(Resource.Error(e))

        }
    }

    suspend fun acceptRequest(requestId: Int) {
        try {
            requestRemoteDataSource.updateRequestStatus(requestId, RequestStatus.Accepted.name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun deleteRequest(requestId: Int) {
        try {
            requestRemoteDataSource.deleteRequest(requestId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun deleteRequestByEventId(eventId: Int) {
        try {
            requestRemoteDataSource.deleteRequestsByEventId(eventId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteAppointment(requestId: Int) {
        try {
            requestRemoteDataSource.deleteAppointment(requestId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}