package com.example.bachelorthesisapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.events.CreateEventEvent
import com.example.bachelorthesisapp.data.model.states.CreateEventFormState
import com.example.bachelorthesisapp.data.model.validators.CreateEventFormValidator
import com.example.bachelorthesisapp.data.remote.Resource
import com.example.bachelorthesisapp.data.repo.ClientRepository
import com.example.bachelorthesisapp.data.repo.auth.AuthRepository
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val clientRepository: ClientRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    // CREATE POST STATE
    var createEventState by mutableStateOf(CreateEventFormState())
    private val createEventValidator: CreateEventFormValidator = CreateEventFormValidator()
    private val validationCreateEventEventChannel = Channel<ValidationEvent>()
    val validationCreateEventEvents = validationCreateEventEventChannel.receiveAsFlow()


    // LOADING STATE
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    // ALL EVENTS BY UID STATE
    val eventState: Flow<UiState<List<Event>>> =
        clientRepository.eventFlow.map { eventEntities ->
            when (eventEntities) {
                is Resource.Error -> {
                    _isLoading.value = false
                    UiState.Error(eventEntities.exception)
                }

                is Resource.Loading -> {
                    _isLoading.value = true
                    UiState.Loading
                }

                is Resource.Success -> {
                    _isLoading.value = false
                    UiState.Success(eventEntities.data)
                }
            }
        }

    // UPCOMING EVENTS STATE
    val eventUpcomingState: Flow<UiState<List<Event>>> =
        clientRepository.eventUpcomingFlow.map { eventEntities ->
            when (eventEntities) {
                is Resource.Error -> {
                    _isLoading.value = false
                    UiState.Error(eventEntities.exception)
                }

                is Resource.Loading -> {
                    _isLoading.value = true
                    UiState.Loading
                }

                is Resource.Success -> {
                    _isLoading.value = false
                    UiState.Success(eventEntities.data)
                }
            }
        }

    // PLANNING EVENTS STATE
    val eventPlanningState: Flow<UiState<List<Event>>> =
        clientRepository.eventPlanningFlow.map { eventEntities ->
            when (eventEntities) {
                is Resource.Error -> {
                    _isLoading.value = false
                    UiState.Error(eventEntities.exception)
                }

                is Resource.Loading -> {
                    _isLoading.value = true
                    UiState.Loading
                }

                is Resource.Success -> {
                    _isLoading.value = false
                    UiState.Success(eventEntities.data)
                }
            }
        }

    // TODAY EVENTS STATE
    val eventTodayState: Flow<UiState<List<Event>>> =
        clientRepository.eventTodayFlow.map { eventEntities ->
            when (eventEntities) {
                is Resource.Error -> {
                    _isLoading.value = false
                    UiState.Error(eventEntities.exception)
                }

                is Resource.Loading -> {
                    _isLoading.value = true
                    UiState.Loading
                }

                is Resource.Success -> {
                    _isLoading.value = false
                    UiState.Success(eventEntities.data)
                }
            }
        }

    // PAST EVENTS STATE
    val eventPastState: Flow<UiState<List<Event>>> =
        clientRepository.eventTodayFlow.map { eventEntities ->
            when (eventEntities) {
                is Resource.Error -> {
                    _isLoading.value = false
                    UiState.Error(eventEntities.exception)
                }

                is Resource.Loading -> {
                    _isLoading.value = true
                    UiState.Loading
                }

                is Resource.Success -> {
                    _isLoading.value = false
                    UiState.Success(eventEntities.data)
                }
            }
        }


    // CURRENT EVENT STATE
    val eventCurrentState: Flow<UiState<Event>> =
        clientRepository.eventCurrentFlow.map { eventResult ->
            when (eventResult) {
                is Resource.Error -> UiState.Error(eventResult.exception)
                is Resource.Loading -> UiState.Loading
                is Resource.Success -> UiState.Success(eventResult.data!!)
            }
        }

    fun loadEventData() {
        viewModelScope.launch {
            val clientId = authRepository.currentUser?.uid!!
            clientRepository.fetchEventsByOrganizerId(clientId)
        }
    }

    fun findEventById(id: Int) {
        viewModelScope.launch {
            clientRepository.fetchEventById(id)
            delay(2000L)
        }
    }

    fun onCreateEventEvent(event: CreateEventEvent) {
        when (event) {
            is CreateEventEvent.TitleChanged -> {
                createEventState = createEventState.copy(title = event.title)
            }

            is CreateEventEvent.DescriptionChanged -> {
                createEventState = createEventState.copy(description = event.description)
            }

            is CreateEventEvent.DateChanged -> {
                createEventState = createEventState.copy(date = event.date)
            }

            is CreateEventEvent.TimeChanged -> {
                createEventState = createEventState.copy(time = event.time)
            }

            is CreateEventEvent.TypeChanged -> {
                createEventState = createEventState.copy(type = event.type)
            }

            is CreateEventEvent.BudgetChanged -> {
                createEventState = createEventState.copy(budget = event.budget)
            }

            is CreateEventEvent.GuestNumberChanged -> {
                createEventState = createEventState.copy(guestNumber = event.guestNumber)
            }

            is CreateEventEvent.VendorsChanged -> {
                createEventState = createEventState.copy(vendors = event.vendors)
            }

            is CreateEventEvent.Submit -> {
                submitCreateEventForm()
            }

        }
    }

    private fun submitCreateEventForm() {

    }


    sealed class ValidationEvent {
        object Success : ValidationEvent()
        object Failure : ValidationEvent()
    }
}