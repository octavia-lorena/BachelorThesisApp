package com.example.bachelorthesisapp.data.datasource

import android.util.Log
import com.example.bachelorthesisapp.data.remote.ActivityDto
import com.example.bachelorthesisapp.data.remote.Api
import com.example.bachelorthesisapp.datasource.ApiResponse
import com.example.bachelorthesisapp.datasource.RemoteDataSource
import com.example.bachelorthesisapp.exception.NetworkCallException
import javax.inject.Inject
import retrofit2.Response

class RemoteDataSourceImpl @Inject constructor(
    private val api: Api
) : RemoteDataSource {

    private suspend fun <T> handleApiResponse(execute: suspend () -> Response<T>): ApiResponse<T> = try {
        val response = execute()
        if (response.isSuccessful) {
            response.body()?.let {
                ApiResponse.Success(it)
            } ?: ApiResponse.SuccessWithoutBody
        } else {
            ApiResponse.Error(NetworkCallException(response.code().toString()))
        }
    } catch (ex: Exception) {
        Log.e(RemoteDataSourceImpl::class.simpleName, ex.stackTraceToString())
        ApiResponse.Error(exception = ex)
    }

    override suspend fun getActivityData(): ApiResponse<ActivityDto> =
        handleApiResponse { api.getActivityData() }
}