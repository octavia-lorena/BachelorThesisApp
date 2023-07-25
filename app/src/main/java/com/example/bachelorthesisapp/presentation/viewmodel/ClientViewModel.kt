package com.example.bachelorthesisapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.BusinessType
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.EventStatus
import com.example.bachelorthesisapp.data.model.entities.EventType
import com.example.bachelorthesisapp.data.model.entities.OfferPost
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
import java.time.LocalDate

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val clientRepository: ClientRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    init {
        deleteAllEvents()
    }

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

    // EVENT RESULT STATE
    val eventResultState: Flow<UiState<Event>> =
        clientRepository.eventResultFlow.map { eventResult ->
            when (eventResult) {
                is Resource.Error -> UiState.Error(eventResult.exception)
                is Resource.Loading -> UiState.Loading
                is Resource.Success -> UiState.Success(eventResult.data)
            }
        }

    // PAST EVENTS STATE
    val businessesState: Flow<UiState<List<BusinessEntity>>> =
        clientRepository.businessFlow.map { businessEntities ->
            when (businessEntities) {
                is Resource.Error -> {
                    _isLoading.value = false
                    UiState.Error(businessEntities.exception)
                }

                is Resource.Loading -> {
                    _isLoading.value = true
                    UiState.Loading
                }

                is Resource.Success -> {
                    _isLoading.value = false
                    UiState.Success(businessEntities.data)
                }
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

    fun findBusinessesByType(businessType: String) {
        viewModelScope.launch {
            clientRepository.fetchBusinessesByType(businessType)
            delay(2000L)
        }
    }

    fun loadBusinesses() {
        viewModelScope.launch {
            clientRepository.fetchBusinesses()
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

            is CreateEventEvent.PartialSubmit -> {
                partialSubmitCreateEventForm()
            }

            is CreateEventEvent.Submit -> {
                submitCreateEventForm()
            }

        }
    }

    private fun partialSubmitCreateEventForm() {
        val titleResult = createEventValidator.validateTitle(createEventState.title)
        val descriptionResult =
            createEventValidator.validateDescription(createEventState.description)
        val typeResult = createEventValidator.validateType(createEventState.type)
        val dateResult = createEventValidator.validateDate(createEventState.date)
        val timeResult = createEventValidator.validateTime(createEventState.time)

        val hasError = listOf(
            titleResult,
            descriptionResult,
            typeResult,
            dateResult,
            timeResult,
        ).any { !it.success }
        if (hasError) {
            createEventState = createEventState.copy(
                titleError = titleResult.errorMessage,
                descriptionError = descriptionResult.errorMessage,
                typeError = typeResult.errorMessage,
                dateError = dateResult.errorMessage,
                timeError = timeResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            validationCreateEventEventChannel.send(ValidationEvent.Success)
        }

    }

    private fun submitCreateEventForm() {
        val guestNumberResult =
            createEventValidator.validateGuestNumber(createEventState.guestNumber)
        val budgetResult = createEventValidator.validateBudget(createEventState.budget)
        val vendorsResult = createEventValidator.validateVendors(createEventState.vendors)
        val hasError = listOf(
            guestNumberResult,
            budgetResult,
            vendorsResult
        ).any { !it.success }
        if (hasError) {
            createEventState = createEventState.copy(
                guestNumberError = guestNumberResult.errorMessage,
                budgetError = budgetResult.errorMessage,
                vendorsError = vendorsResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            val name = createEventState.title
            val description = createEventState.description
            val type = createEventState.type
            val date = createEventState.date
            val time = createEventState.time
            val guestNumber = createEventState.guestNumber
            val budget = createEventState.budget
            val vendors = createEventState.vendors
            val vendorTypesStrings = vendors.split(";")
            val vendorTypes = enumValues<BusinessType>().filter { it.name in vendorTypesStrings }
            val organizerId = authRepository.currentUser?.uid!!
            delay(2000L)
            val event = Event(
                id = 0,
                organizerId = organizerId,
                name = name,
                description = description,
                type = enumValues<EventType>().find { it.name == type }!!,
                date = LocalDate.parse(date),
                time = time,
                guestNumber = guestNumber.toInt(),
                budget = budget.toInt(),
                cost = 0,
                vendors = vendorTypes.associateWith { -1 },
                status = EventStatus.Planning
            )
            Log.d("NEW_EVENT", event.toString())
            createEvent(event)
            delay(2000L)
            eventResultState.collect {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        validationCreateEventEventChannel.send(ValidationEvent.Success)
                    }

                    is UiState.Error -> {
                        Log.d("CREATE_EVENT", it.cause.stackTraceToString())
                        validationCreateEventEventChannel.send(ValidationEvent.Failure)
                    }
                }
            }
            validationCreateEventEventChannel.send(ValidationEvent.Success)
            createEventState = createEventState.copy(
                title = "",
                titleError = null,
                description = "",
                descriptionError = null,
                date = "",
                dateError = null,
                time = "",
                timeError = null,
                guestNumber = "",
                guestNumberError = null,
                budget = "",
                budgetError = null,
                vendors = "",
                vendorsError = null,
                type = "",
                typeError = null
            )
        }

    }

    private fun createEvent(event: Event) {
        viewModelScope.launch {
            clientRepository.createEvent(event)
        }
    }

    private fun deleteAllEvents() {
        viewModelScope.launch {
            clientRepository.deleteAllEvents()
        }
    }


    sealed class ValidationEvent {
        object Success : ValidationEvent()
        object Failure : ValidationEvent()
    }
}