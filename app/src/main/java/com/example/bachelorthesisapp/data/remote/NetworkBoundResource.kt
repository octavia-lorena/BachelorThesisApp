package com.example.bachelorthesisapp.data.remote

@Suppress("unused")
suspend inline fun <ResultType, RequestType> networkCall(
    crossinline localSource: suspend () -> ResultType,
    crossinline remoteSource: suspend () -> RequestType,
    crossinline compareData: suspend (RequestType, ResultType) -> Unit,
    crossinline onResult: suspend (Resource<RequestType>) -> Unit
) {
    onResult(
        try {
            val localData = localSource()
            val remoteData = remoteSource()

            compareData(remoteData, localData)

            Resource.Success(remoteData)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    )
}
