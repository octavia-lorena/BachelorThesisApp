package com.example.bachelorthesisapp.data.repo

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.bachelorthesisapp.data.datasource.BusinessLocalDataSource
import com.example.bachelorthesisapp.data.datasource.BusinessRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.datasource.ClientLocalDataSource
import com.example.bachelorthesisapp.data.datasource.ClientRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.datasource.EventRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.datasource.EventsLocalDataSource
import com.example.bachelorthesisapp.data.datasource.PostRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.datasource.PostsLocalDataSource
import com.example.bachelorthesisapp.data.datasource.RequestRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.datasource.RequestsLocalDataSource
import com.example.bachelorthesisapp.data.model.entities.AppointmentRequest
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.ClientEntity
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.EventStatus
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.model.entities.RequestStatus
import com.example.bachelorthesisapp.data.remote.Resource
import com.example.bachelorthesisapp.data.remote.networkCall
import com.example.bachelorthesisapp.data.remote.toEntity
import com.example.bachelorthesisapp.data.repo.firebase.PushNotification
import com.example.bachelorthesisapp.exception.NetworkCallException
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import java.time.LocalDate

class ClientRepository @Inject constructor(
    private val eventsLocalDataSource: EventsLocalDataSource,
    private val businessLocalDataSource: BusinessLocalDataSource,
    private val clientLocalDataSource: ClientLocalDataSource,
    private val postsLocalDataSource: PostsLocalDataSource,
    private val requestsLocalDataSource: RequestsLocalDataSource,
    private val eventRemoteDataSource: EventRemoteDataSourceImpl,
    private val postRemoteDataSource: PostRemoteDataSourceImpl,
    private val businessRemoteDataSource: BusinessRemoteDataSourceImpl,
    private val requestRemoteDataSource: RequestRemoteDataSourceImpl,
    private val clientRemoteDataSource: ClientRemoteDataSourceImpl

) {

    // EVENTS FLOW
    private val _eventListFlow = MutableSharedFlow<Resource<List<Event>>>()
    val eventListFlow: Flow<Resource<List<Event>>> = _eventListFlow

    // EVENTS PLANNING FLOW
    private val _eventPlanningFlow = MutableSharedFlow<Resource<List<Event>>>()
    val eventPlanningFlow: Flow<Resource<List<Event>>> = _eventPlanningFlow

    // EVENTS UPCOMING FLOW
    private val _eventUpcomingFlow = MutableSharedFlow<Resource<List<Event>>>()
    val eventUpcomingFlow: Flow<Resource<List<Event>>> = _eventUpcomingFlow

    // CURRENT EVENT FLOW
//    private val _eventCurrentFlow = MutableSharedFlow<Resource<Event?>>()
//    val eventCurrentFlow: Flow<Resource<Event?>> = _eventCurrentFlow
    private val _eventCurrentFlow: MutableState<Resource<Event>> =
        mutableStateOf(Resource.Loading())
    val eventCurrentFlow: State<Resource<Event>> = _eventCurrentFlow

    // EVENTS TODAY FLOW
    private val _eventTodayFlow = MutableSharedFlow<Resource<List<Event>>>()
    val eventTodayFlow: Flow<Resource<List<Event>>> = _eventTodayFlow

    // EVENTS PAST FLOW
    private val _eventPastFlow = MutableSharedFlow<Resource<List<Event>>>()
    val eventPastFlow: Flow<Resource<List<Event>>> = _eventPastFlow

    // EVENTS PAST FLOW
    private val _eventPastBusinessFlow = MutableSharedFlow<Resource<List<Event>>>()
    val eventPastBusinessFlow: Flow<Resource<List<Event>>> = _eventPastBusinessFlow

    // EVENT RESULT FLOW
    private val _eventResultFlow = MutableSharedFlow<Resource<Event>>()
    val eventResultFlow: Flow<Resource<Event>> = _eventResultFlow

    // BUSINESS FLOW
    private val _businessFlow = MutableSharedFlow<Resource<List<BusinessEntity>>>()
    val businessFlow: Flow<Resource<List<BusinessEntity>>> = _businessFlow

    // BUSINESS BY TYPE FLOW
    private val _businessByTypeFlow = MutableSharedFlow<Resource<List<BusinessEntity>>>()
    val businessByTypeFlow: Flow<Resource<List<BusinessEntity>>> = _businessByTypeFlow

    // BUSINESS BY CITY FLOW
    private val _businessByCityFlow = MutableSharedFlow<Resource<List<BusinessEntity>>>()
    val businessByCityFlow: Flow<Resource<List<BusinessEntity>>> = _businessByCityFlow

    // BUSINESS RESULT FLOW
    private val _businessResultFlow = MutableSharedFlow<Resource<BusinessEntity>>()
    val businessResultFlow: Flow<Resource<BusinessEntity>> = _businessResultFlow

    // POSTS BY BUSINESS ID FLOW
    private val _postBusinessFlow = MutableSharedFlow<Resource<List<OfferPost>>>()
    val postBusinessFlow: Flow<Resource<List<OfferPost>>> = _postBusinessFlow

    // POST BY ID FLOW
//    private val _postFlow = MutableSharedFlow<Resource<OfferPost>>()
//    val postFlow: Flow<Resource<OfferPost>> = _postFlow
    private val _postFlow: MutableState<Resource<OfferPost>> = mutableStateOf(Resource.Loading())
    val postFlow: State<Resource<OfferPost>> = _postFlow

    // REQUESTS FLOW (REQUEST STATUS = PENDING)
    private val _requestFlow = MutableSharedFlow<Resource<List<AppointmentRequest>>>()
    val requestFlow: Flow<Resource<List<AppointmentRequest>>> = _requestFlow

    // REQUESTS FLOW (REQUEST STATUS = ACCEPTED)
    private val _appointmentFlow = MutableSharedFlow<Resource<List<AppointmentRequest>>>()
    val appointmentFlow: Flow<Resource<List<AppointmentRequest>>> = _appointmentFlow

    // REQUEST RESULT FLOW
    private val _requestResultFlow = MutableSharedFlow<Resource<AppointmentRequest>>()
    val requestResultFlow: Flow<Resource<AppointmentRequest>> = _requestResultFlow

    // CLIENT BY ID FLOW
    private val _clientFlow = MutableSharedFlow<Resource<ClientEntity>>()
    val clientFlow: Flow<Resource<ClientEntity>> = _clientFlow

    private val _deletedEventFlow = MutableSharedFlow<Resource<Event>>()
    val deletedEventFlow: Flow<Resource<Event>> = _deletedEventFlow

    // ALL CLIENTS FLOW
    private val _clientsFlow = MutableSharedFlow<Resource<List<ClientEntity>>>()
    val clientsFlow: Flow<Resource<List<ClientEntity>>> = _clientsFlow

    // ALL POSTS FLOW
    private val _postsFlow = MutableSharedFlow<Resource<List<OfferPost>>>()
    val postsFlow: Flow<Resource<List<OfferPost>>> = _postsFlow


    private suspend fun fetchEvents() = networkCall(
        localSource = {
            eventsLocalDataSource.getAllEntities().also { Log.d("EVENTS", "local $it") }
        },
        remoteSource = {
            eventRemoteDataSource.getAllEvents().also { Log.d("EVENTS", "remote $it") }
        },
        compareData = { remoteBusiness, localBusiness ->
            if (remoteBusiness != localBusiness) {
                val eventEntities = remoteBusiness.map { it.toEntity() }
                eventsLocalDataSource.repopulateEntities(eventEntities)
            }
        },
        onResult = {
        }
    )

    suspend fun fetchAllEvents() = networkCall(
        localSource = {
            eventsLocalDataSource.getAllEntities().also { Log.d("EVENTS", "local $it") }
        },
        remoteSource = {
            eventRemoteDataSource.getAllEvents().also { Log.d("EVENTS", "remote $it") }
        },
        compareData = { remoteBusiness, localBusiness ->
            if (remoteBusiness != localBusiness) {
                val eventEntities = remoteBusiness.map { it.toEntity() }
                eventsLocalDataSource.repopulateEntities(eventEntities)
            }
        },
        onResult = { events ->
            _eventListFlow.emit(
                when (events) {
                    is Resource.Error -> {
                        Log.d("EVENTS", "ERROR")
                        Resource.Error<Exception>(events.exception)
                        val eventsList = eventsLocalDataSource.getAllEntities()
                        Resource.Success(eventsList
                            .sortedBy { it.date }
                            .filter { it.status != EventStatus.Past })
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Log.d("EVENTS", "SUCCESS")
                        Resource.Success(events.data
                            .map { it.toEntity() }
                            .sortedBy { it.date }
                            .filter { it.status != EventStatus.Past })
                    }
                }
            )
            _eventPastBusinessFlow.emit(
                when (events) {
                    is Resource.Error -> {
                        Log.d("EVENTS", "ERROR")
                        Resource.Error<Exception>(events.exception)
                        val eventsList = eventsLocalDataSource.getAllEntities()
                        Resource.Success(eventsList
                            .filter { it.status == EventStatus.Past })
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Log.d("EVENTS", "SUCCESS")
                        Resource.Success(events.data
                            .map { it.toEntity() }
                            .filter { it.status == EventStatus.Past })
                    }
                }
            )
        }
    )

    suspend fun fetchEventsByOrganizerId(organizerId: String) = networkCall(
        localSource = { eventsLocalDataSource.getEventsByOrganizerId(organizerId) },
        remoteSource = { eventRemoteDataSource.getEventByOrganizerId(organizerId) },
        compareData = { _, _ ->
            fetchEvents()
            delay(1000L)
        },
        onResult = { events ->
            _eventListFlow.emit(
                when (events) {
                    is Resource.Error -> {
                        Log.d("EVENTS", "ERROR")
                        Resource.Error<Exception>(events.exception)
                        val eventsList = eventsLocalDataSource.getEventsByOrganizerId(organizerId)
                        Resource.Success(eventsList
                            .sortedBy { it.date }
                            .filter { it.status != EventStatus.Past })
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Log.d("EVENTS", "SUCCESS")
                        Resource.Success(events.data
                            .map { it.toEntity() }
                            .sortedBy { it.date }
                            .filter { it.status != EventStatus.Past })
                    }
                }
            )
            _eventPlanningFlow.emit(
                when (events) {
                    is Resource.Error -> {
                        Log.d("EVENTS", "ERROR")
                        Resource.Error<Exception>(events.exception)
                        val eventsList = eventsLocalDataSource.getEventsByOrganizerId(organizerId)
                        Resource.Success(eventsList
                            .sortedBy { it.date }
                            .filter { it.status == EventStatus.Planning })
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Log.d("EVENTS", "SUCCESS")
                        Resource.Success(events.data
                            .map { it.toEntity() }
                            .sortedBy { it.date }
                            .filter { it.status == EventStatus.Planning })
                    }
                }
            )
            _eventUpcomingFlow.emit(
                when (events) {
                    is Resource.Error -> {
                        Log.d("EVENTS", "ERROR")
                        Resource.Error<Exception>(events.exception)
                        val eventsList = eventsLocalDataSource.getEventsByOrganizerId(organizerId)
                        Resource.Success(eventsList
                            .sortedBy { it.date }
                            .filter { it.status == EventStatus.Upcoming }
                            .filter { it.date != LocalDate.now() })

                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Log.d("EVENTS", "SUCCESS")
                        Resource.Success(events.data
                            .map { it.toEntity() }
                            .sortedBy { it.date }
                            .filter { it.status == EventStatus.Upcoming })
                    }
                }
            )

            _eventTodayFlow.emit(
                when (events) {
                    is Resource.Error -> {
                        Log.d("EVENTS", "ERROR")
                        Resource.Error<Exception>(events.exception)
                        val eventsList = eventsLocalDataSource.getEventsByOrganizerId(organizerId)
                        Resource.Success(
                            eventsList
                                .filter { it.date == LocalDate.now() }
                                .filter { it.status == EventStatus.Upcoming })
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Log.d("EVENTS", "SUCCESS")
                        Resource.Success(events.data
                            .map { it.toEntity() }
                            .filter { it.date == LocalDate.now() }
                            .filter { it.status == EventStatus.Upcoming })
                    }
                }
            )

            _eventPastFlow.emit(
                when (events) {
                    is Resource.Error -> {
                        Log.d("EVENTS", "ERROR")
                        Resource.Error<Exception>(events.exception)
                        val eventsList = eventsLocalDataSource.getEventsByOrganizerId(organizerId)
                        Resource.Success(
                            eventsList
                                .filter { it.status == EventStatus.Past })
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Log.d("EVENTS", "SUCCESS")
                        Resource.Success(events.data
                            .map { it.toEntity() }
                            .filter { it.status == EventStatus.Past })
                    }
                }
            )

        }
    )

    suspend fun fetchEventById(eventId: Int) = networkCall(
        localSource = {
            eventsLocalDataSource.getEntity(eventId.toString())
                .also { Log.d("EVENT", "local $it") }
        },
        remoteSource = {
            eventRemoteDataSource.getEventById(eventId)
                .also { Log.d("EVENT", "remote $it") }
        },
        compareData = { _, _ ->
            // fetchEvents()
            delay(2000L)
        },
        onResult = { event ->
            //    _eventCurrentFlow.emit(
            when (event) {
                is Resource.Error -> {
                    _eventCurrentFlow.value = Resource.Error(event.exception)
                    val posts = eventsLocalDataSource.getEntity(eventId.toString())!!
                    _eventCurrentFlow.value = Resource.Success(posts)
                }

                is Resource.Loading -> _eventCurrentFlow.value = Resource.Loading()
                is Resource.Success -> _eventCurrentFlow.value =
                    Resource.Success(event.data.toEntity())
            }
            //  )
        }
    )

    suspend fun createEvent(event: Event) {
        try {
            _eventResultFlow.emit(Resource.Loading())
            val result = eventRemoteDataSource.addEvent(event)
            _eventResultFlow.emit(Resource.Success(result.toEntity()))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CREATE_EVENT", e.stackTraceToString())
            _eventResultFlow.emit(Resource.Error(e))

        }
    }

    suspend fun updateEvent(
        id: Int,
        name: String,
        description: String,
        date: String,
        time: String,
        guestNumber: String,
        budget: String
    ) {
        try {
            _eventResultFlow.emit(Resource.Loading())
            val result = eventRemoteDataSource.updateEvent(
                id = id,
                name = name,
                description = description,
                date = date,
                time = time,
                guestNumber = guestNumber.toInt(),
                budget = budget.toInt()
            )
            _eventResultFlow.emit(Resource.Success(result.toEntity()))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("UPDATE_EVENT", e.stackTraceToString())
            _eventResultFlow.emit(Resource.Error(e))

        }
    }

    suspend fun deleteAllEvents() {
        eventsLocalDataSource.deleteAllEntities()
    }

    suspend fun deleteAllPosts() {
        postsLocalDataSource.deleteAllEntities()
    }

    suspend fun fetchBusinesses() = networkCall(
        localSource = {
            businessLocalDataSource.getAllEntities()
        },
        remoteSource = {
            businessRemoteDataSource.getAllBusiness()
                .also { Log.d("BUSINESSES_REMOTE", it.toString()) }
        },
        compareData = { remoteBusiness, localBusiness ->
            if (remoteBusiness != localBusiness) {
                val entities = remoteBusiness.map { it.toEntity() }
                businessLocalDataSource.repopulateEntities(entities)
            }
        },
        onResult = { businesses ->
            _businessFlow.emit(
                when (businesses) {
                    is Resource.Error -> {
                        Log.d("BUSINESSES", "ERROR")
                        Resource.Error<Exception>(businesses.exception)
                        val businessesList =
                            businessLocalDataSource.getAllEntities()
                        Resource.Success(businessesList)
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Log.d("BUSINESSES", "SUCCESS")
                        Resource.Success(businesses.data
                            .map { it.toEntity() })
                    }
                }
            )
        }
    )


    suspend fun fetchBusinessesByType(businessType: String) = networkCall(
        localSource = {
            businessLocalDataSource.getBusinessesByType(businessType)
                .also { Log.d("FILTER_VENDORS", "local $it") }
        },
        remoteSource = {
            businessRemoteDataSource.getBusinessByType(businessType)
                .also { Log.d("FILTER_VENDORS", "remote $it") }
        },
        compareData = { _, _ ->
            fetchBusinesses()
            delay(5000L)
        },
        onResult = { businesses ->
            _businessByTypeFlow.emit(
                when (businesses) {
                    is Resource.Error -> {
                        Log.d("BUSINESSES", "ERROR")
                        Resource.Error<Exception>(businesses.exception)
                        val businessesList =
                            businessLocalDataSource.getBusinessesByType(businessType)
                        Resource.Success(businessesList
                            .filter { it.businessType.name == businessType })
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Log.d("BUSINESSES", "SUCCESS")
                        Resource.Success(businesses.data
                            .map { it.toEntity() }
                            .filter { it.businessType.name == businessType })
                    }
                }
            )
        })

    suspend fun fetchBusinessesByCity(city: String) = networkCall(
        localSource = {
            businessLocalDataSource.getBusinessesByCity(city)
        },
        remoteSource = {
            businessRemoteDataSource.getBusinessByCity(city)
        },
        compareData = { _, _ ->
            fetchBusinesses()
            delay(2000L)
        },
        onResult = { businesses ->
            _businessByCityFlow.emit(
                when (businesses) {
                    is Resource.Error -> {
                        Resource.Error<Exception>(businesses.exception)
                        val businessesList =
                            businessLocalDataSource.getBusinessesByCity(city)
                        Resource.Success(businessesList
                            .filter { it.city == city })
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Resource.Success(businesses.data
                            .map { it.toEntity() }
                            .filter { it.city == city })
                    }
                }
            )
        })


    suspend fun filterBusinessesByCity(city: String) {
        _businessFlow.collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    _businessByCityFlow.emit(Resource.Success(resource.data.filter { it.city == city }))
                }

                is Resource.Loading -> {
                    _businessByCityFlow.emit(resource)
                }

                is Resource.Error -> {
                    _businessByCityFlow.emit(resource)
                }
            }
        }


    }

    suspend fun fetchBusinessById(businessId: String) = networkCall(
        localSource = {
            businessLocalDataSource.getEntity(businessId)
        },
        remoteSource = {
            businessRemoteDataSource.getBusinessById(businessId)
        },
        compareData = { _, _ ->
            fetchBusinesses()
            delay(5000L)
        },
        onResult = { business ->
            _businessResultFlow.emit(
                when (business) {
                    is Resource.Error -> {
                        Resource.Error<Exception>(business.exception)
                        val businessLocal =
                            businessLocalDataSource.getEntity(businessId)
                        Resource.Success(businessLocal!!)
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Resource.Success(business.data.toEntity())
                    }
                }
            )
        })

    suspend fun fetchClients() = networkCall(
        localSource = { clientLocalDataSource.getAllEntities() },
        remoteSource = { clientRemoteDataSource.getAllClients() },
        compareData = { remoteBusiness, localBusiness ->
            if (remoteBusiness != localBusiness) {
                val businessEntities = remoteBusiness.map { it.toEntity() }
                clientLocalDataSource.repopulateEntities(businessEntities)
            }
        },
        onResult = { clients ->
            _clientsFlow.emit(
                when (clients) {
                    is Resource.Error -> {
                        Resource.Error<Exception>(clients.exception)
                        val clientsLocal =
                            clientLocalDataSource.getAllEntities()
                        Resource.Success(clientsLocal)
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Resource.Success(clients.data.map { it.toEntity() })
                    }
                }
            )
        }
    )

    suspend fun fetchClientById(clientId: String) = networkCall(
        localSource = {
            clientLocalDataSource.getEntity(clientId)
        },
        remoteSource = {
            clientRemoteDataSource.getClientById(clientId)
        },
        compareData = { _, _ ->
            fetchClients()
            delay(2000L)
        },
        onResult = { client ->
            _clientFlow.emit(
                when (client) {
                    is Resource.Error -> {
                        Resource.Error<Exception>(client.exception)
                        val clientLocal =
                            clientLocalDataSource.getEntity(clientId)
                        Resource.Success(clientLocal!!)
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> {
                        Resource.Success(client.data.toEntity())
                    }
                }
            )
        })


    suspend fun fetchPostsByBusinessId(businessId: String) = networkCall(
        localSource = { postsLocalDataSource.getPostsByBusinessId(businessId) },
        remoteSource = { postRemoteDataSource.getPostByBusinessId(businessId) },
        compareData = { _, _ ->
            fetchAllPosts()
        },
        onResult = { activity ->
            //fetchAllPosts()
            _postBusinessFlow.emit(
                when (activity) {
                    is Resource.Error -> {
                        Resource.Error<Exception>(activity.exception)
                        val posts = postsLocalDataSource.getPostsByBusinessId(businessId)
                        Resource.Success(posts)
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> Resource.Success(activity.data.map { it.toEntity() })
                }
            )
        }
    )

    suspend fun fetchPostById(id: Int) = networkCall(
        localSource = { postsLocalDataSource.getEntity(id.toString()) },
        remoteSource = { postRemoteDataSource.getPostById(id) },
        compareData = { _, _ ->
            fetchAllPosts()
            delay(5000L)
        },
        onResult = { post ->
            when (post) {
                is Resource.Error -> {
                    _postFlow.value = Resource.Error(post.exception)
                    val postLocal = postsLocalDataSource.getEntity(id.toString())!!
                    _postFlow.value = Resource.Success(postLocal)
                }

                is Resource.Loading -> _postFlow.value = Resource.Loading()
                is Resource.Success -> _postFlow.value = Resource.Success(post.data.toEntity())
            }

        }
    )

    suspend fun fetchAllPosts() = networkCall(
        localSource = { postsLocalDataSource.getAllEntities() },
        remoteSource = { postRemoteDataSource.getAllPost() },
        compareData = { remoteBusiness, localBusiness ->
            if (remoteBusiness != localBusiness) {
                Log.d("POSTS", "data updates")
                val businessEntities = remoteBusiness.map { it.toEntity() }
                postsLocalDataSource.repopulateEntities(businessEntities)
            }
        },
        onResult = { posts ->
            _postsFlow.emit(
                when (posts) {
                    is Resource.Success -> {
                        Resource.Success(posts.data.map { it.toEntity() })
                    }

                    is Resource.Loading -> {
                        Resource.Loading()
                    }

                    is Resource.Error -> {
                        Resource.Error<Exception>(posts.exception)
                        val postsLocal =
                            postsLocalDataSource.getAllEntities()
                        Resource.Success(postsLocal)
                    }
                }
            )
        }
    )

    private suspend fun fetchAllRequests() = networkCall(
        localSource = { requestsLocalDataSource.getAllEntities() },
        remoteSource = { requestRemoteDataSource.getAllRequests() },
        compareData = { remoteRequests, localRequests ->
            if (remoteRequests != localRequests) {
                Log.d("REQUESTS", "data updates")
                val requestsEntities = remoteRequests.map { it.toEntity() }
                requestsLocalDataSource.repopulateEntities(requestsEntities)
            }
        },
        onResult = {
        }
    )

    suspend fun fetchRequestsByBusinessId(businessId: String) = networkCall(
        localSource = { requestsLocalDataSource.getRequestsByBusinessId(businessId = businessId) },
        remoteSource = { requestRemoteDataSource.getRequestsByBusinessId(businessId) },
        compareData = { _, _ ->
            fetchAllRequests()
        },
        onResult = { requests ->
            _requestFlow.emit(
                when (requests) {
                    is Resource.Success -> {
                        Resource.Success(requests.data.map { it.toEntity() }
                            .filter { it.status == RequestStatus.Pending })
                    }

                    is Resource.Loading -> {
                        Resource.Loading()
                    }

                    is Resource.Error -> {
                        Resource.Error<Exception>(requests.exception)
                        val requestsLocal =
                            requestsLocalDataSource.getRequestsByBusinessId(businessId = businessId)
                        Resource.Success(requestsLocal.filter { it.status == RequestStatus.Pending })
                    }
                }
            )
            _appointmentFlow.emit(
                when (requests) {
                    is Resource.Success -> {
                        Resource.Success(requests.data.map { it.toEntity() }
                            .filter { it.status == RequestStatus.Accepted })
                    }

                    is Resource.Loading -> {
                        Resource.Loading()
                    }

                    is Resource.Error -> {
                        Resource.Error<Exception>(requests.exception)
                        val requestsLocal =
                            requestsLocalDataSource.getRequestsByBusinessId(businessId = businessId)
                        Resource.Success(requestsLocal.filter { it.status == RequestStatus.Accepted })
                    }
                }
            )
        }
    )

    suspend fun createRequest(appointmentRequest: AppointmentRequest) {
        try {
            _requestResultFlow.emit(Resource.Loading())
            val result = requestRemoteDataSource.addRequest(appointmentRequest)
            _requestResultFlow.emit(Resource.Success(result.toEntity()))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CREATE_REQUEST", e.stackTraceToString())
            _requestResultFlow.emit(Resource.Error(e))

        }
    }

    suspend fun sendNotification(pushNotification: PushNotification) =
        businessRemoteDataSource.sendNotification(pushNotification)

    suspend fun acceptRequest(requestId: Int) {
        try {
            requestRemoteDataSource.updateRequestStatus(requestId, RequestStatus.Accepted.name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun setVendorValue(eventId: Int, category: String, postId: Int) {
        try {
            eventRemoteDataSource.setVendorForCategory(eventId, category, postId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun setEventCost(eventId: Int, price: Int){
        try {
            eventRemoteDataSource.setEventCost(eventId = eventId, price = price)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteRequest(requestId: Int) {
        try {
            requestRemoteDataSource.deleteRequest(requestId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteEvent(eventId: Int) {
        try {
            val deletedEvent = eventRemoteDataSource.deleteEvent(eventId)
            if (deletedEvent.id == -1)
                _deletedEventFlow.emit(Resource.Error(NetworkCallException("You cannot delete an event taking place in less than 30 days!")))
            else _deletedEventFlow.emit(Resource.Success(deletedEvent.toEntity()))
            Log.d("EVENT", "Deleted event: $deletedEvent")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteRequestByEventId(eventId: Int) {
        try {
            requestRemoteDataSource.deleteRequestsByEventId(eventId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun ratePost(postId: Int, ratingValue: Int) {
        try {
            postRemoteDataSource.ratePost(postId, ratingValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun publishEvent(eventId: Int){
        try {
            eventRemoteDataSource.publishEvent(eventId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}