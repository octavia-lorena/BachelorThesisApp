package com.example.bachelorthesisapp.data.businesses

import android.util.Log
import com.example.bachelorthesisapp.data.events.remote.EventRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.posts.remote.PostRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.posts.local.PostsLocalDataSource
import com.example.bachelorthesisapp.data.appointment_requests.remote.RequestRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.core.remote.networkCall
import com.example.bachelorthesisapp.data.businesses.local.BusinessLocalDataSource
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.data.businesses.remote.BusinessRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.businesses.remote.dto.toEntity
import com.example.bachelorthesisapp.data.notifications.PushNotification
import com.example.bachelorthesisapp.data.posts.remote.dto.toEntity
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest

class BusinessRepository @Inject constructor(
    private val businessLocalDataSource: BusinessLocalDataSource,
    private val businessRemoteDataSource: BusinessRemoteDataSourceImpl,
) {

    /// BUSINESS FLOW
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





    suspend fun sendNotification(pushNotification: PushNotification) =
        businessRemoteDataSource.sendNotification(pushNotification)

}