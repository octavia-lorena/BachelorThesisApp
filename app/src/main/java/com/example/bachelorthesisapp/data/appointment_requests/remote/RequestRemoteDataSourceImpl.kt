package com.example.bachelorthesisapp.data.appointment_requests.remote

import android.util.Log
import com.example.bachelorthesisapp.core.remote.ApiResponse
import com.example.bachelorthesisapp.core.remote.NetworkCallException
import com.example.bachelorthesisapp.core.remote.RemoteDataSource
import com.example.bachelorthesisapp.core.resources.RemoteResource
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.appointment_requests.remote.api.RequestApi
import com.example.bachelorthesisapp.data.appointment_requests.remote.dto.AppointmentRequestDto
import com.example.bachelorthesisapp.data.posts.remote.dto.OfferPostDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import retrofit2.Response

class RequestRemoteDataSourceImpl @Inject constructor(
    private val api: RequestApi,
) : RemoteDataSource() {
    suspend fun getAllRequests(): List<AppointmentRequestDto> = api.getAllRequests()
    suspend fun addRequest(appointmentRequest: AppointmentRequest): AppointmentRequestDto =
        api.addRequest(appointmentRequest)

    suspend fun getRequestsByBusinessId(businessId: String): RemoteResource<List<AppointmentRequestDto>> {
        return withContext(Dispatchers.IO) {
            makeCall {
                api.getRequestsByBusinessId(businessId)
            }
        }
    }

    suspend fun deleteRequest(requestId: Int): AppointmentRequestDto =
        api.deleteRequest(requestId)

    suspend fun updateRequestStatus(id: Int, status: String): AppointmentRequestDto =
        api.updateRequestStatus(id, status)

    suspend fun deleteRequestsByEventId(eventId: Int): List<AppointmentRequestDto> =
        api.deleteRequestsByEventId(eventId)

    suspend fun deleteAppointment(requestId: Int): AppointmentRequestDto =
        api.deleteAppointment(requestId)
}