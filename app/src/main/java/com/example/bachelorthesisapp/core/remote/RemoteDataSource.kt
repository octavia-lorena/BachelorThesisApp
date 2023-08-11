package com.example.bachelorthesisapp.core.remote

import com.example.bachelorthesisapp.core.resources.RemoteResource
import java.net.HttpURLConnection
import retrofit2.Response

abstract class RemoteDataSource {

    internal suspend fun <R : Any> makeCall(call: suspend () -> Response<R>): RemoteResource<R> {
        return try {
            val response = call()

            if (response.code() == HttpURLConnection.HTTP_OK) {
                val body = response.body()
                if (body != null) {
                    RemoteResource.Success(body)
                } else {
                    RemoteResource.Error(NetworkEmptyResponseException())
                }
            } else {
                RemoteResource.Error(NetworkApiException(response.code()))
            }
        } catch (e: Exception) {
            RemoteResource.Error(e)
        }
    }
}