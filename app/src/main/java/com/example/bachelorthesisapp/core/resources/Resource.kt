package com.example.bachelorthesisapp.core.resources

sealed class Resource<out T: Any> {

    data class Success<out T: Any>(val value: T) : Resource<T>()
    data class Error(val cause: Exception) : Resource<Nothing>()
    data class Loading<out T: Any>(val data: T? = null) : Resource<T>()
}
