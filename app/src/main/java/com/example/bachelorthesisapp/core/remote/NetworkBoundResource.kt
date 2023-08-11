package com.example.bachelorthesisapp.core.remote

import com.example.bachelorthesisapp.core.resources.Resource

@Suppress("unused")
suspend inline fun <ResultType, RequestType> networkCall(
    crossinline localSource: suspend () -> ResultType,
    crossinline remoteSource: suspend () -> RequestType,
    crossinline compareData: suspend (RequestType, ResultType) -> Unit,
    crossinline onResult: suspend (Resource<RequestType>) -> Unit
) {
    onResult(
        try {
            Resource.Loading(null)

            val localData = localSource()
            val remoteData = remoteSource()

            compareData(remoteData, localData)

            Resource.Success(remoteData)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    )
}
