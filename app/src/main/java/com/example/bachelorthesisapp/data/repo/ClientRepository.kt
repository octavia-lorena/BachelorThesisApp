package com.example.bachelorthesisapp.data.repo

import android.util.Log
import com.example.bachelorthesisapp.data.datasource.BusinessLocalDataSource
import com.example.bachelorthesisapp.data.datasource.EventsLocalDataSource
import com.example.bachelorthesisapp.data.datasource.RemoteDataSourceImpl
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.EventStatus
import com.example.bachelorthesisapp.data.remote.Resource
import com.example.bachelorthesisapp.data.remote.networkCall
import com.example.bachelorthesisapp.data.remote.toEntity
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.LocalDate

class ClientRepository @Inject constructor(
    private val eventsLocalDataSource: EventsLocalDataSource,
    private val businessLocalDataSource: BusinessLocalDataSource,
    private val remoteDataSource: RemoteDataSourceImpl,
) {

    // EVENTS FLOW
    private val _eventFlow = MutableSharedFlow<Resource<List<Event>>>()
    val eventFlow: Flow<Resource<List<Event>>> = _eventFlow

    // EVENTS PLANNING FLOW
    private val _eventPlanningFlow = MutableSharedFlow<Resource<List<Event>>>()
    val eventPlanningFlow: Flow<Resource<List<Event>>> = _eventPlanningFlow

    // EVENTS UPCOMING FLOW
    private val _eventUpcomingFlow = MutableSharedFlow<Resource<List<Event>>>()
    val eventUpcomingFlow: Flow<Resource<List<Event>>> = _eventUpcomingFlow

    // CURRENT EVENT FLOW
    private val _eventCurrentFlow = MutableSharedFlow<Resource<Event?>>()
    val eventCurrentFlow: Flow<Resource<Event?>> = _eventCurrentFlow

    // EVENTS TODAY FLOW
    private val _eventTodayFlow = MutableSharedFlow<Resource<List<Event>>>()
    val eventTodayFlow: Flow<Resource<List<Event>>> = _eventTodayFlow

    // EVENTS PAST FLOW
    private val _eventPastFlow = MutableSharedFlow<Resource<List<Event>>>()
    val eventPastFlow: Flow<Resource<List<Event>>> = _eventPastFlow

    // EVENT RESULT FLOW
    private val _eventResultFlow = MutableSharedFlow<Resource<Event>>()
    val eventResultFlow: Flow<Resource<Event>> = _eventResultFlow

    // EVENTS PAST FLOW
    private val _businessFlow = MutableSharedFlow<Resource<List<BusinessEntity>>>()
    val businessFlow: Flow<Resource<List<BusinessEntity>>> = _businessFlow


    private suspend fun fetchEvents() = networkCall(
        localSource = {
            eventsLocalDataSource.getAllEntities().also { Log.d("EVENTS", "local $it") }
        },
        remoteSource = { remoteDataSource.getEventData().also { Log.d("EVENTS", "remote $it") } },
        compareData = { remoteBusiness, localBusiness ->
            if (remoteBusiness != localBusiness) {
                val eventEntities = remoteBusiness.map { it.toEntity() }
                eventsLocalDataSource.repopulateEntities(eventEntities)
            }
        },
        onResult = {
        }
    )

    suspend fun fetchEventsByOrganizerId(organizerId: String) = networkCall(
        localSource = { eventsLocalDataSource.getEventsByOrganizerId(organizerId) },
        remoteSource = { remoteDataSource.getEventDataByOrganizerId(organizerId) },
        compareData = { _, _ ->
            fetchEvents()
            delay(1000L)
        },
        onResult = { events ->
            _eventFlow.emit(
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
        localSource = { eventsLocalDataSource.getEntity(eventId.toString()) },
        remoteSource = { remoteDataSource.getEventDataById(eventId) },
        compareData = { _, _ ->
        },
        onResult = { event ->
            _eventCurrentFlow.emit(
                when (event) {
                    is Resource.Error -> {
                        Resource.Error<Exception>(event.exception)
                        val posts = eventsLocalDataSource.getEntity(eventId.toString())
                        Resource.Success(posts)
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> Resource.Success(event.data.toEntity())
                }
            )
        }
    )

    suspend fun createEvent(event: Event) {
        try {
            _eventResultFlow.emit(Resource.Loading())
            val result = remoteDataSource.addEvent(event)
            _eventResultFlow.emit(Resource.Success(result.toEntity()))
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("CREATE_EVENT", e.stackTraceToString())
            _eventResultFlow.emit(Resource.Error(e))

        }
    }

    suspend fun deleteAllEvents() {
        eventsLocalDataSource.deleteAllEntities()
    }

    suspend fun fetchBusinesses() = networkCall(
        localSource = {
            businessLocalDataSource.getAllEntities()
        },
        remoteSource = {
            remoteDataSource.getBusinessData().also { Log.d("BUSINESSES_REMOTE", it.toString()) }
        },
        compareData = { remoteBusiness, localBusiness ->
            if (remoteBusiness != localBusiness) {
                val entities = remoteBusiness.map { it.toEntity() }
                businessLocalDataSource.repopulateEntities(entities)
            }
        },
        onResult = {
                businesses ->
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
            remoteDataSource.getBusinessDataByType(businessType)
                .also { Log.d("FILTER_VENDORS", "remote $it") }
        },
        compareData = { _, _ ->
            fetchBusinesses()
            delay(2000L)
        },
        onResult = { businesses ->
            _businessFlow.emit(
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

}