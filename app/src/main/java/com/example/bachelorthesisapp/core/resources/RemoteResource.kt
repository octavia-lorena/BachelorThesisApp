package com.example.bachelorthesisapp.core.resources

sealed class RemoteResource<out T : Any> {
    data class Success<out T : Any>(val value: T) : RemoteResource<T>()
    data class Error(val cause: Exception) : RemoteResource<Nothing>()
}

fun <T : Any> RemoteResource<T>.toResource(): Resource<T> {
    return when (this) {
        is RemoteResource.Success -> {
            Resource.Success(this.value)
        }

        is RemoteResource.Error -> {
            Resource.Error(this.cause)
        }
    }
}