package com.example.bachelorthesisapp.core.remote

sealed class ApiResponse<out T> {
    data class Success<T>(val data: T) : ApiResponse<T>()
    object SuccessWithoutBody : ApiResponse<Nothing>()
    data class Error(
        val exception: Exception
    ) : ApiResponse<Nothing>()
}
