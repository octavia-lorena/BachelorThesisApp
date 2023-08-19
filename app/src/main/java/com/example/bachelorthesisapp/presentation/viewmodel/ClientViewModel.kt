package com.example.bachelorthesisapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.data.model.BusinessType
import com.example.bachelorthesisapp.data.clients.local.entity.ClientEntity
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.model.EventStatus
import com.example.bachelorthesisapp.data.model.EventType
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.model.RequestStatus
import com.example.bachelorthesisapp.data.model.events.CreateEventEvent
import com.example.bachelorthesisapp.data.model.events.UpdateEventEvent
import com.example.bachelorthesisapp.data.model.states.CreateEventFormState
import com.example.bachelorthesisapp.data.model.states.UpdateEventFormState
import com.example.bachelorthesisapp.data.model.validators.CreateEventFormValidator
import com.example.bachelorthesisapp.data.model.validators.UpdateEventFormValidator
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.data.clients.ClientRepository
import com.example.bachelorthesisapp.data.authentication.AuthRepository
import com.example.bachelorthesisapp.data.notifications.NotificationData
import com.example.bachelorthesisapp.data.notifications.PushNotification
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.data.appointment_requests.AppointmentRequestsRepository
import com.example.bachelorthesisapp.data.businesses.BusinessRepository
import com.example.bachelorthesisapp.data.events.EventsRepository
import com.example.bachelorthesisapp.data.posts.OfferPostsRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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
    private val postsRepository: OfferPostsRepository,
    private val eventsRepository: EventsRepository,
    private val requestsRepository: AppointmentRequestsRepository,
    private val businessRepository: BusinessRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    val database: DatabaseReference =
        Firebase.database.getReference(AuthRepository.USERS_TABLE_NAME)

    init {
        // deleteAllEvents()
        //deleteAllPosts()
        viewModelScope.launch {
            loadAllEventsByOrganizerId()
            loadBusinesses()
            loadPostsByBusinessId()
            loadAllRequests()
        }

    }

    // CREATE EVENT STATE
    var createEventState by mutableStateOf(CreateEventFormState())
    private val createEventValidator: CreateEventFormValidator = CreateEventFormValidator()
    private val validationCreateEventEventChannel = Channel<ValidationEvent>()
    val validationCreateEventEvents = validationCreateEventEventChannel.receiveAsFlow()

    // UPDATE POST STATE
    var updateEventState by mutableStateOf(UpdateEventFormState())
    private val updateEventValidator: UpdateEventFormValidator = UpdateEventFormValidator()
    private val validationUpdateEventEventChannel = Channel<ValidationEvent>()
    val validationUpdateEventEvents = validationUpdateEventEventChannel.receiveAsFlow()


    // LOADING STATE
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _postState = MutableStateFlow<OfferPost?>(null)
    val postState2 = _postState.asStateFlow()

    // ALL EVENTS BY UID STATE
    val eventState: Flow<UiState<List<Event>>> =
        eventsRepository.eventListFlow.map { eventEntities ->
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
        eventsRepository.eventUpcomingFlow.map { eventEntities ->
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
        eventsRepository.eventPlanningFlow.map { eventEntities ->
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
        eventsRepository.eventTodayFlow.map { eventEntities ->
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
        eventsRepository.eventPastFlow.map { eventEntities ->
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
    val eventPastBusinessState: Flow<UiState<List<Event>>> =
        eventsRepository.eventPastFlow.map { eventEntities ->
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
//    val eventCurrentState: Flow<UiState<Event>> =
//        clientRepository.eventCurrentFlow.map { eventResult ->
//            when (eventResult) {
//                is Resource.Error -> UiState.Error(eventResult.exception)
//                is Resource.Loading -> UiState.Loading
//                is Resource.Success -> UiState.Success(eventResult.data!!)
//            }
//        }
    val eventCurrentState = eventsRepository.eventCurrentFlow


    // EVENT RESULT STATE
    val eventResultState: Flow<UiState<Event>> =
        eventsRepository.eventResultFlow.map { eventResult ->
            when (eventResult) {
                is Resource.Error -> {
                    _isLoading.value = false
                    UiState.Error(eventResult.exception)
                }

                is Resource.Loading -> {
                    _isLoading.value = true
                    UiState.Loading
                }

                is Resource.Success -> {
                    _isLoading.value = false
                    UiState.Success(eventResult.data)
                }
            }
        }

    // BUSINESSES STATE
    val businessesState: Flow<UiState<List<BusinessEntity>>> =
        businessRepository.businessFlow.map { businessEntities ->
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

    // BUSINESSES BY TYPE STATE
    val businessesByTypeState: Flow<UiState<List<BusinessEntity>>> =
        businessRepository.businessByTypeFlow.map { businessEntities ->
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

    // BUSINESSES BY CITY STATE
    val businessesByCityState: Flow<UiState<List<BusinessEntity>>> =
        businessRepository.businessByCityFlow.map { businessEntities ->
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

    // BUSINESS RESULT STATE
    val businessResultState: Flow<UiState<BusinessEntity>> =
        businessRepository.businessResultFlow.map { eventResult ->
            when (eventResult) {
                is Resource.Error -> UiState.Error(eventResult.exception)
                is Resource.Loading -> UiState.Loading
                is Resource.Success -> UiState.Success(eventResult.data)
            }
        }

    // POSTS BY BUSINESS ID STATE
    val postBusinessState: Flow<UiState<List<OfferPost>>> =
        postsRepository.postBusinessFlow.map { postEntities ->
            when (postEntities) {
                is Resource.Error -> {
                    _isLoading.value = false
                    UiState.Error(postEntities.exception)
                }

                is Resource.Loading -> {
                    _isLoading.value = true
                    UiState.Loading
                }

                is Resource.Success -> {
                    _isLoading.value = false
                    UiState.Success(postEntities.data)
                }
            }
        }

    // POST BY ID STATE
    val postState = postsRepository.postFlow
//        when (clientRepository.postFlow.value) {
//        is Resource.Loading -> {
//            UiState.Loading
//        }
//
//        is Resource.Error -> {
//            UiState.Error((clientRepository.postFlow.value as Resource.Error<OfferPost>).exception)
//        }
//
//        is Resource.Success -> {
//            UiState.Success((clientRepository.postFlow.value as Resource.Success<OfferPost>).data)
//        }
//    }


    // REQUESTS STATE (REQUEST STATUS = PENDING)
    val requestsState: Flow<UiState<List<AppointmentRequest>>> =
        requestsRepository.requestFlow.map { requestEntities ->
            when (requestEntities) {
                is Resource.Error -> {
                    _isLoading.value = false
                    UiState.Error(requestEntities.exception)
                }

                is Resource.Loading -> {
                    _isLoading.value = true
                    UiState.Loading
                }

                is Resource.Success -> {
                    _isLoading.value = false
                    UiState.Success(requestEntities.data)
                }
            }
        }

    // APPOINTMENTS STATE (REQUEST STATUS = ACCEPTED)
    val appointmentsState: Flow<UiState<List<AppointmentRequest>>> =
        requestsRepository.appointmentFlow.map { requestEntities ->
            when (requestEntities) {
                is Resource.Error -> {
                    _isLoading.value = false
                    UiState.Error(requestEntities.exception)
                }

                is Resource.Loading -> {
                    _isLoading.value = true
                    UiState.Loading
                }

                is Resource.Success -> {
                    _isLoading.value = false
                    UiState.Success(requestEntities.data)
                }
            }
        }

    // REQUEST RESULT STATE
    val requestResultState: Flow<UiState<AppointmentRequest>> =
        requestsRepository.requestResultFlow.map { requestResult ->
            when (requestResult) {
                is Resource.Error -> UiState.Error(requestResult.exception)
                is Resource.Loading -> UiState.Loading
                is Resource.Success -> UiState.Success(requestResult.data)
            }
        }

    // CLIENT BY ID STATE
    val clientState: Flow<UiState<ClientEntity>> =
        clientRepository.clientFlow.map { result ->
            when (result) {
                is Resource.Error -> UiState.Error(result.exception)
                is Resource.Loading -> UiState.Loading
                is Resource.Success -> UiState.Success(result.data)
            }
        }

    val deletedEventState: Flow<UiState<Event>> =
        eventsRepository.deletedEventFlow.map { result ->
            when (result) {
                is Resource.Error -> UiState.Error(result.exception)
                is Resource.Loading -> UiState.Loading
                is Resource.Success -> UiState.Success(result.data)
            }
        }

    // ALL CLIENTS STATE
    val clientsState: Flow<UiState<List<ClientEntity>>> =
        clientRepository.clientsFlow.map { result ->
            when (result) {
                is Resource.Error -> UiState.Error(result.exception)
                is Resource.Loading -> UiState.Loading
                is Resource.Success -> UiState.Success(result.data)
            }
        }

    // ALL POSTS STATE
    val postsState: Flow<UiState<List<OfferPost>>> =
        postsRepository.postsFlow.map { result ->
            when (result) {
                is Resource.Error -> {
                    _isLoading.value = false
                    UiState.Error(result.exception)
                }

                is Resource.Loading -> {
                    _isLoading.value = true
                    UiState.Loading
                }

                is Resource.Success -> {
                    _isLoading.value = false
                    UiState.Success(result.data)
                }
            }
        }


    suspend fun loadAllEventsByOrganizerId() {
        viewModelScope.launch {
            val clientId = authRepository.currentUser?.uid
            delay(2000L)
            if (clientId != null) {
                eventsRepository.fetchEventsByOrganizerId(clientId)
            }
        }
    }

    suspend fun loadAllEvents() {
        viewModelScope.launch {
            eventsRepository.fetchAllEvents()
            delay(2000L)
        }
    }

    suspend fun loadAllClients() {
        viewModelScope.launch {
            clientRepository.fetchClients()
            delay(2000L)
        }
    }

    suspend fun loadAllPosts() {
        viewModelScope.launch {
            postsRepository.fetchAllPosts()
            delay(2000L)
        }
    }

    private suspend fun loadPostsByBusinessId() {
        viewModelScope.launch {
            val clientId = authRepository.currentUser?.uid
            delay(2000L)
            if (clientId != null) {
                postsRepository.fetchPostsByBusinessId(clientId)
            }
        }
    }

    private suspend fun loadAllRequests() {
        viewModelScope.launch {
            val clientId = authRepository.currentUser?.uid
            delay(2000L)
            if (clientId != null) {
                requestsRepository.fetchRequestsByBusinessId(clientId)
            }
        }
    }


    fun findEventById(id: Int) {
        viewModelScope.launch {
            eventsRepository.fetchEventById(id)
            delay(2000L)
        }
    }

    suspend fun findPostsByBusinessId(businessId: String) {
        viewModelScope.launch {
            postsRepository.fetchPostsByBusinessId(businessId)
            delay(2000L)
        }
    }

    fun findPostById(id: Int) {
        viewModelScope.launch {
            postsRepository.fetchPostById(id)
            delay(2000L)
        }
    }

    suspend fun findBusinessById(id: String) {
        viewModelScope.launch {
            businessRepository.fetchBusinessById(id)
            delay(2000L)
        }
    }

    suspend fun findClientById(id: String) {
        viewModelScope.launch {
            clientRepository.fetchClientById(id)
            delay(2000L)
        }
    }

    fun findBusinessesByType(businessType: String) {
        viewModelScope.launch {
            businessRepository.fetchBusinessesByType(businessType)
            delay(2000L)
        }
    }

    fun findBusinessesByCity(city: String) {
        viewModelScope.launch {
            businessRepository.fetchBusinessesByCity(city)
            delay(2000L)
        }
    }

    suspend fun loadBusinesses() {
        viewModelScope.launch {
            businessRepository.fetchBusinesses()
            delay(2000L)
        }
    }

    suspend fun loadRequests(businessId: String) {
        viewModelScope.launch {
            requestsRepository.fetchRequestsByBusinessId(businessId)
            delay(2000L)
        }
    }

    fun onCreateEventEvent(event: CreateEventEvent) {
        when (event) {
            is CreateEventEvent.TitleChanged -> {
                createEventState = createEventState.copy(title = event.title)
                validateTitleCreateEventForm()
            }

            is CreateEventEvent.DescriptionChanged -> {
                createEventState = createEventState.copy(description = event.description)
                validateDescriptionCreateEventForm()
            }

            is CreateEventEvent.DateChanged -> {
                createEventState = createEventState.copy(date = event.date)
                validateDateCreateEventForm()
            }

            is CreateEventEvent.TimeChanged -> {
                createEventState = createEventState.copy(time = event.time)
                validateTimeCreateEventForm()
            }

            is CreateEventEvent.TypeChanged -> {
                createEventState = createEventState.copy(type = event.type)
                validateTypeCreateEventForm()
            }

            is CreateEventEvent.BudgetChanged -> {
                createEventState = createEventState.copy(budget = event.budget)
                validateBudgetCreateEventForm()
            }

            is CreateEventEvent.GuestNumberChanged -> {
                createEventState = createEventState.copy(guestNumber = event.guestNumber)
                validateGuestNumberCreateEventForm()
            }

            is CreateEventEvent.VendorsChanged -> {
                createEventState = createEventState.copy(vendors = event.vendors)
                validateVendorsCreateEventForm()
            }

            is CreateEventEvent.PartialSubmit -> {
                partialSubmitCreateEventForm()
            }

            is CreateEventEvent.Submit -> {
                submitCreateEventForm()
            }

        }
    }

    private fun validateVendorsCreateEventForm() {
        val result = createEventValidator.validateVendors(createEventState.vendors)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            createEventState = createEventState.copy(
                vendorsError = result.errorMessage,
            )
            return
        } else {
            createEventState = createEventState.copy(
                vendorsError = null,
            )
            return
        }
    }

    private fun validateBudgetCreateEventForm() {
        val result = createEventValidator.validateBudget(createEventState.budget)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            createEventState = createEventState.copy(
                budgetError = result.errorMessage,
            )
            return
        } else {
            createEventState = createEventState.copy(
                budgetError = null,
            )
            return
        }
    }

    private fun validateBudgetUpdateEventForm() {
        val result = updateEventValidator.validateBudget(updateEventState.budget)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            updateEventState = updateEventState.copy(
                budgetError = result.errorMessage,
            )
            return
        } else {
            updateEventState = updateEventState.copy(
                budgetError = null,
            )
            return
        }
    }

    private fun validateGuestNumberCreateEventForm() {
        val result = createEventValidator.validateGuestNumber(createEventState.guestNumber)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            createEventState = createEventState.copy(
                guestNumberError = result.errorMessage,
            )
            return
        } else {
            createEventState = createEventState.copy(
                guestNumberError = null,
            )
            return
        }
    }

    private fun validateGuestNumberUpdateEventForm() {
        val result = updateEventValidator.validateGuestNumber(updateEventState.guestNumber)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            updateEventState = updateEventState.copy(
                guestNumberError = result.errorMessage,
            )
            return
        } else {
            updateEventState = updateEventState.copy(
                guestNumberError = null,
            )
            return
        }
    }

    private fun validateTypeCreateEventForm() {
        val result = createEventValidator.validateType(createEventState.type)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            createEventState = createEventState.copy(
                typeError = result.errorMessage,
            )
            return
        } else {
            createEventState = createEventState.copy(
                typeError = null,
            )
            return
        }
    }

    private fun validateTimeCreateEventForm() {
        val result = createEventValidator.validateTime(createEventState.time)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            createEventState = createEventState.copy(
                timeError = result.errorMessage,
            )
            return
        } else {
            createEventState = createEventState.copy(
                timeError = null,
            )
            return
        }
    }

    private fun validateTimeUpdateEventForm() {
        val result = updateEventValidator.validateTime(updateEventState.time)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            updateEventState = updateEventState.copy(
                timeError = result.errorMessage,
            )
            return
        } else {
            updateEventState = updateEventState.copy(
                timeError = null,
            )
            return
        }
    }

    private fun validateDateCreateEventForm() {
        val result = createEventValidator.validateDate(createEventState.date)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            createEventState = createEventState.copy(
                dateError = result.errorMessage,
            )
            return
        } else {
            createEventState = createEventState.copy(
                dateError = null,
            )
            return
        }
    }

    private fun validateDateUpdateEventForm() {
        val result = updateEventValidator.validateDate(updateEventState.date)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            updateEventState = updateEventState.copy(
                dateError = result.errorMessage,
            )
            return
        } else {
            updateEventState = updateEventState.copy(
                dateError = null,
            )
            return
        }
    }

    private fun validateDescriptionCreateEventForm() {
        val result = createEventValidator.validateDescription(createEventState.description)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            createEventState = createEventState.copy(
                descriptionError = result.errorMessage,
            )
            return
        } else {
            createEventState = createEventState.copy(
                descriptionError = null,
            )
            return
        }
    }

    private fun validateDescriptionUpdateEventForm() {
        val result = updateEventValidator.validateDescription(updateEventState.description)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            updateEventState = updateEventState.copy(
                descriptionError = result.errorMessage,
            )
            return
        } else {
            updateEventState = updateEventState.copy(
                descriptionError = null,
            )
            return
        }
    }

    private fun validateTitleCreateEventForm() {
        val result = createEventValidator.validateTitle(createEventState.title)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            createEventState = createEventState.copy(
                titleError = result.errorMessage,
            )
            return
        } else {
            createEventState = createEventState.copy(
                titleError = null,
            )
            return
        }
    }

    private fun validateTitleUpdateEventForm() {
        val result = updateEventValidator.validateTitle(updateEventState.name)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            updateEventState = updateEventState.copy(
                nameError = result.errorMessage,
            )
            return
        } else {
            updateEventState = updateEventState.copy(
                nameError = null,
            )
            return
        }
    }

    fun onUpdateEventEvent(event: UpdateEventEvent) {
        when (event) {
            is UpdateEventEvent.NameChanged -> {
                updateEventState = updateEventState.copy(name = event.name)
                validateTitleUpdateEventForm()
            }

            is UpdateEventEvent.DescriptionChanged -> {
                updateEventState = updateEventState.copy(description = event.description)
                validateDescriptionUpdateEventForm()
            }

            is UpdateEventEvent.DateChanged -> {
                updateEventState = updateEventState.copy(date = event.date)
                validateDateUpdateEventForm()
            }

            is UpdateEventEvent.TimeChanged -> {
                updateEventState = updateEventState.copy(time = event.time)
                validateTimeUpdateEventForm()
            }

            is UpdateEventEvent.BudgetChanged -> {
                updateEventState = updateEventState.copy(budget = event.budget)
                validateBudgetUpdateEventForm()
            }

            is UpdateEventEvent.GuestNumberChanged -> {
                updateEventState = updateEventState.copy(guestNumber = event.guestNumber)
                validateGuestNumberUpdateEventForm()
            }

            is UpdateEventEvent.Submit -> {
                submitUpdateEventForm()
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
         //   validationCreateEventEventChannel.send(ValidationEvent.Success)
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

    private fun submitUpdateEventForm() {
        val titleResult = updateEventValidator.validateTitle(updateEventState.name)
        val descriptionResult =
            updateEventValidator.validateDescription(updateEventState.description)
        val dateResult = updateEventValidator.validateDate(updateEventState.date)
        val timeResult = updateEventValidator.validateTime(updateEventState.time)
        val guestNumberResult =
            updateEventValidator.validateGuestNumber(updateEventState.guestNumber)
        val budgetResult = updateEventValidator.validateBudget(updateEventState.budget)
        val hasError = listOf(
            titleResult,
            descriptionResult,
            dateResult,
            timeResult,
            guestNumberResult,
            budgetResult,
        ).any { !it.success }
        if (hasError) {
            updateEventState = updateEventState.copy(
                nameError = titleResult.errorMessage,
                descriptionError = descriptionResult.errorMessage,
                dateError = dateResult.errorMessage,
                timeError = timeResult.errorMessage,
                guestNumberError = guestNumberResult.errorMessage,
                budgetError = budgetResult.errorMessage,
            )
            return
        }

        viewModelScope.launch {
            val id = updateEventState.id
            val name = updateEventState.name
            val description = updateEventState.description
            val date = updateEventState.date
            val time = updateEventState.time
            val guestNumber = updateEventState.guestNumber
            val budget = updateEventState.budget
            updateEvent(
                id = id,
                name = name,
                description = description,
                date = date,
                time = time,
                guestNumber = guestNumber,
                budget = budget
            )
            delay(2000L)
            eventResultState.collect {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        validationUpdateEventEventChannel.send(ValidationEvent.Success)
                    }

                    is UiState.Error -> {
                        validationUpdateEventEventChannel.send(ValidationEvent.Failure)
                    }
                }
            }
            updateEventState = updateEventState.copy(
                name = "",
                nameError = null,
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
            )
        }

    }

    fun setUpdateEventState(event: Event) {
        viewModelScope.launch {
            updateEventState = updateEventState.copy(
                name = event.name,
                nameError = null,
                description = event.description,
                descriptionError = null,
                date = event.date.toString(),
                dateError = null,
                time = event.time,
                timeError = null,
                guestNumber = event.guestNumber.toString(),
                guestNumberError = null,
                budget = event.budget.toString(),
                budgetError = null,
            )
            delay(1000L)
        }

    }

    private fun createEvent(event: Event) {
        viewModelScope.launch {
            eventsRepository.createEvent(event)
        }
    }

    private fun deleteAllEvents() {
        viewModelScope.launch {
            eventsRepository.deleteAllEvents()
        }
    }

    private fun deleteAllPosts() {
        viewModelScope.launch {
            postsRepository.deleteAllPosts()
        }
    }

    private fun updateEvent(
        id: Int,
        name: String,
        description: String,
        date: String,
        time: String,
        guestNumber: String,
        budget: String
    ) {
        viewModelScope.launch {
            eventsRepository.updateEvent(
                id = id,
                name = name,
                description = description,
                date = date,
                time = time,
                guestNumber = guestNumber,
                budget = budget
            )
            delay(2000L)
        }
    }


    fun createRequest(eventId: Int, postId: Int, pushNotification: PushNotification) {
        viewModelScope.launch {
            val request = AppointmentRequest(0, eventId, postId, RequestStatus.Pending)
            requestsRepository.createRequest(request)
        }
        sendNotification(pushNotification)
        Log.d("BUSINESS_TOKEN", pushNotification.to)
    }

    private fun sendNotification(pushNotification: PushNotification) {
        viewModelScope.launch {
            try {
                val response = businessRepository.sendNotification(pushNotification)
                Log.d("TAG", "SUCCESS ${response.raw().body.toString()}")
            } catch (e: Exception) {
                Log.e("TAG", "Error: ${e.stackTraceToString()}")
            }
        }
    }

    fun acceptRequest(
        requestId: Int,
        business: BusinessEntity,
        event: Event,
        post: OfferPost,
        clientDeviceId: String
    ) {
        viewModelScope.launch {
            //val username = database.child(AuthRepository.CLIENTS_TABLE_NAME).child(clientId).child("username").get().await().value
            requestsRepository.acceptRequest(requestId = requestId)
            eventsRepository.setVendorValue(event.id, business.businessType.name, post.id)
            eventsRepository.setEventCost(eventId = event.id, price = post.price)

        }
        sendNotification(
            PushNotification(
                data = NotificationData(
                    "Accepted Request",
                    "${business.businessName} accepted your request for event ${event.name}"
                ), to = clientDeviceId
            )
        )
    }


    fun declineRequest(
        requestId: Int,
        businessName: String,
        eventName: String,
        clientDeviceId: String
    ) {
        viewModelScope.launch {
            requestsRepository.deleteRequest(requestId)
        }
        sendNotification(
            PushNotification(
                data = NotificationData(
                    "Declined Request",
                    "$businessName declined your request for event $eventName"
                ), to = clientDeviceId
            )
        )
    }


    fun deleteEvent(eventId: Int) {
        viewModelScope.launch {
            eventsRepository.deleteEvent(eventId = eventId)
            requestsRepository.deleteRequestByEventId(eventId = eventId)
        }
    }

    fun ratePost(postId: Int, ratingValue: Int) {
        viewModelScope.launch {
            postsRepository.ratePost(postId, ratingValue)
        }
    }

    fun publishEvent(eventId: Int) {
        viewModelScope.launch {
            eventsRepository.publishEvent(eventId)
        }
    }

    fun cancelAppointment(
        requestId: Int,
        business: BusinessEntity,
        event: Event,
        post: OfferPost,
        clientDeviceId: String
    ) {
        viewModelScope.launch {
            // delete request
            requestsRepository.deleteAppointment(requestId)
            // update event - set vendor value for the category to -1, reset the event cost <- cost - post.price
            eventsRepository.setVendorValue(event.id, business.businessType.name, -1)
            eventsRepository.setEventCost(event.id, -post.price)

        }
        sendNotification(
            PushNotification(
                data = NotificationData(
                    "Canceled appointment",
                    "${business.businessName}, ${business.businessType} canceled the appointment for ${event.name}, with ${post.title}"
                ), to = clientDeviceId
            )
        )
    }

    fun clearUpdateEventState() {
        viewModelScope.launch {
            updateEventState = updateEventState.copy(
                name = "",
                nameError = null,
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
            )
        }
    }

    fun clearCreateEventState() {
        viewModelScope.launch {
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
                type = "",
                typeError = null,
                vendors = "",
                vendorsError = null
            )
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
        object Failure : ValidationEvent()
    }
}