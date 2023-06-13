package com.example.bachelorthesisapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorthesisapp.datasource.ApiResponse
import com.example.bachelorthesisapp.datasource.RemoteDataSource
import com.example.bachelorthesisapp.presentation.viewmodel.model.ActivityModel
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {

    private val activityMutableState =
        MutableStateFlow<UiState<ActivityModel>>(UiState.Loading)
    val activityState: StateFlow<UiState<ActivityModel>> = activityMutableState

    fun loadActivityData() {
        viewModelScope.launch {
            when (val result = remoteDataSource.getActivityData()) {
                is ApiResponse.Success -> {
                    activityMutableState.value = UiState.Success(result.data.toActivityModel())
                }

                is ApiResponse.Error -> {
                    activityMutableState.value = UiState.Error(result.exception)
                }

                ApiResponse.SuccessWithoutBody -> {
                    // no implementation
                }
            }
        }
    }
}