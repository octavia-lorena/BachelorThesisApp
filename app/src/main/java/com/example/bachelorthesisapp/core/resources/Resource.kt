package com.example.bachelorthesisapp.core.resources

sealed class Resource<T> {

    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val exception: Exception, val data: T? = null) : Resource<T>()
    class Loading<T>(val data: T? = null) : Resource<T>()
}
