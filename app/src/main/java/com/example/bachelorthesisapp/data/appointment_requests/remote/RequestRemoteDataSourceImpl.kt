package com.example.bachelorthesisapp.data.appointment_requests.remote

import android.util.Log
import com.example.bachelorthesisapp.core.remote.ApiResponse
import com.example.bachelorthesisapp.core.remote.NetworkCallException
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.appointment_requests.remote.api.RequestApi
import com.example.bachelorthesisapp.data.appointment_requests.remote.dto.AppointmentRequestDto
import javax.inject.Inject
import retrofit2.Response

class RequestRemoteDataSourceImpl @Inject constructor(
    private val api: RequestApi,
) : RequestRemoteDataSource {

    private suspend fun <T> handleApiResponse(execute: suspend () -> Response<T>): ApiResponse<T> =
        try {
            val response = execute()
            if (response.isSuccessful) {
                response.body()?.let {
                    ApiResponse.Success(it)
                } ?: ApiResponse.SuccessWithoutBody
            } else {
                ApiResponse.Error(NetworkCallException(response.code().toString()))
            }
        } catch (ex: Exception) {
            Log.e(RequestRemoteDataSourceImpl::class.simpleName, ex.stackTraceToString())
            ApiResponse.Error(exception = ex)
        }


    override suspend fun getAllRequests(): List<AppointmentRequestDto> = api.getAllRequests()
    override suspend fun addRequest(appointmentRequest: AppointmentRequest): AppointmentRequestDto =
        api.addRequest(appointmentRequest)

    override suspend fun getRequestsByBusinessId(businessId: String): List<AppointmentRequestDto> =
        api.getRequestsByBusinessId(businessId)

    override suspend fun deleteRequest(requestId: Int): AppointmentRequestDto =
        api.deleteRequest(requestId)

    override suspend fun updateRequestStatus(id: Int, status: String): AppointmentRequestDto =
        api.updateRequestStatus(id, status)

    override suspend fun deleteRequestsByEventId(eventId: Int): List<AppointmentRequestDto> =
        api.deleteRequestsByEventId(eventId)

    override suspend fun deleteAppointment(requestId: Int): AppointmentRequestDto = api.deleteAppointment(requestId)
}