package com.example.bachelorthesisapp.core.domain

interface UseCase<S, R> {

    suspend fun execute(
        args: S,
        successCallback: (r: R) -> Unit,
        failureCallback: (e: Exception) -> Unit
    )
}