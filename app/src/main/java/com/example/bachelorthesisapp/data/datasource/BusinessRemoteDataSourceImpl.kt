package com.example.bachelorthesisapp.data.datasource

import android.util.Log
import com.example.bachelorthesisapp.data.remote.BusinessApi
import com.example.bachelorthesisapp.data.remote.BusinessDto
import com.example.bachelorthesisapp.data.remote.NotificationApi
import com.example.bachelorthesisapp.data.repo.firebase.PushNotification
import com.example.bachelorthesisapp.datasource.ApiResponse
import com.example.bachelorthesisapp.datasource.BusinessRemoteDataSource
import com.example.bachelorthesisapp.exception.NetworkCallException
import javax.inject.Inject
import retrofit2.Response

class BusinessRemoteDataSourceImpl @Inject constructor(
    private val api: BusinessApi,
    private val notificationApi: NotificationApi,
) : BusinessRemoteDataSource {

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
            Log.e(BusinessRemoteDataSourceImpl::class.simpleName, ex.stackTraceToString())
            ApiResponse.Error(exception = ex)
        }

    override suspend fun getAllBusiness(): List<BusinessDto> = api.getBusinessesData()
    override suspend fun getBusinessByType(type: String): List<BusinessDto> =
        api.getBusinessesDataByType(type)

    override suspend fun getBusinessByCity(city: String): List<BusinessDto> =
        api.getBusinessesDataByCity(city)


    override suspend fun getBusinessById(businessId: String): BusinessDto =
        api.getBusinessDataById(businessId)


    override suspend fun updateBusinessDeviceToken(id: String, token: String): BusinessDto =
        api.updateBusinessDeviceToken(id, token)

    suspend fun sendNotification(pushNotification: PushNotification) =
        notificationApi.postNotification(pushNotification)

}