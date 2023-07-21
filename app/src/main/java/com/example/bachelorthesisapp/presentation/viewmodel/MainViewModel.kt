package com.example.bachelorthesisapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.model.entities.toModel
import com.example.bachelorthesisapp.data.remote.Resource
import com.example.bachelorthesisapp.data.repo.ActivityRepository
import com.example.bachelorthesisapp.data.repo.BusinessRepository
import com.example.bachelorthesisapp.presentation.viewmodel.model.ActivityModel
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val activityRepository: ActivityRepository,
    private val businessRepository: BusinessRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val activityState: Flow<UiState<ActivityModel>> =
        activityRepository.activityFlow.mapLatest { activityEntities ->
            when (activityEntities) {
                is Resource.Error -> UiState.Error(activityEntities.exception)
                is Resource.Loading -> UiState.Loading
                is Resource.Success -> UiState.Success(activityEntities.data.first().toModel())
            }
        }

    val postState: Flow<UiState<List<OfferPost>>> =
        businessRepository.postFlow.map { activityEntities ->
            when (activityEntities) {
                is Resource.Error -> UiState.Error(activityEntities.exception)
                is Resource.Loading -> UiState.Loading
                is Resource.Success -> UiState.Success(activityEntities.data)
            }
        }

    fun loadActivityData() {
        viewModelScope.launch {
            activityRepository.fetchActivity()
        }
    }

    fun loadPostData() {
        viewModelScope.launch {
            businessRepository.fetchAllPosts()
        }
    }
}