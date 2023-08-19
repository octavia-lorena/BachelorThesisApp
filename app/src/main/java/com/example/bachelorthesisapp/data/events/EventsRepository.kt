package com.example.bachelorthesisapp.data.events

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.bachelorthesisapp.core.remote.NetworkCallException
import com.example.bachelorthesisapp.core.remote.networkCall
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.data.events.local.EventsLocalDataSource
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.events.remote.EventRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.events.remote.dto.toEntity
import com.example.bachelorthesisapp.data.model.EventStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.LocalDate
import javax.inject.Inject

class EventsRepository @Inject constructor(
    private val eventsLocalDataSource: EventsLocalDataSource,
    private val eventRemoteDataSource: EventRemoteDataSourceImpl,
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

    private val _deletedEventFlow = MutableSharedFlow<Resource<Event>>()
    val deletedEventFlow: Flow<Resource<Event>> = _deletedEventFlow

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
          //  delay(2000L)
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

    suspend fun setVendorValue(eventId: Int, category: String, postId: Int) {
        try {
            eventRemoteDataSource.setVendorForCategory(eventId, category, postId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun setEventCost(eventId: Int, price: Int) {
        try {
            eventRemoteDataSource.setEventCost(eventId = eventId, price = price)
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

    suspend fun publishEvent(eventId: Int) {
        try {
            eventRemoteDataSource.publishEvent(eventId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}