package com.example.bachelorthesisapp.data.repo

import com.example.bachelorthesisapp.data.datasource.BusinessLocalDataSource
import com.example.bachelorthesisapp.data.datasource.RemoteDataSourceImpl
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.remote.Resource
import com.example.bachelorthesisapp.data.remote.networkCall
import com.example.bachelorthesisapp.data.remote.toEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class BusinessRepository @Inject constructor(
    private val businessLocalDataSource: BusinessLocalDataSource,
    private val remoteDataSource: RemoteDataSourceImpl,
) {

    private val _businessFlow = MutableSharedFlow<Resource<List<BusinessEntity>>>()
    val businessFlow: Flow<Resource<List<BusinessEntity>>> = _businessFlow

    suspend fun fetchBusinesses() = networkCall(
        localSource = { businessLocalDataSource.getAllEntities() },
        remoteSource = { remoteDataSource.getBusinessData() },
        compareData = { remoteBusiness, localBusiness ->
            if (remoteBusiness != localBusiness) {
                val businessEntities = remoteBusiness.map { it.toEntity() }
                businessLocalDataSource.repopulateEntities(businessEntities)
            }
        },
        onResult = { activity ->
            _businessFlow.emit(
                when (activity) {
                    is Resource.Error -> Resource.Error(activity.exception)
                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> Resource.Success(activity.data.map { it.toEntity() })
                }
            )
        }
    )

    suspend fun fetchBusinessesByType(type: String) = networkCall(
        localSource = { businessLocalDataSource.getBusinessesByType(type) },
        remoteSource = { remoteDataSource.getBusinessData() },
        compareData = { remoteBusiness, localBusiness ->
            if (remoteBusiness != localBusiness) {
                val businessEntities = remoteBusiness.map { it.toEntity() }
                businessLocalDataSource.repopulateEntities(businessEntities)
            }
        },
        onResult = { activity ->
            _businessFlow.emit(
                when (activity) {
                    is Resource.Error -> Resource.Error(activity.exception)
                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> Resource.Success(activity.data.map { it.toEntity() })
                }
            )
        }
    )
}