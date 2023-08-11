package com.example.bachelorthesisapp.core.presentation

sealed class UiState<out T : Any> {
    data class Success<out T : Any>(val value: T) : UiState<T>()
    data class Error(val cause: Exception) : UiState<Nothing>()
    object Loading : UiState<Nothing>()
}