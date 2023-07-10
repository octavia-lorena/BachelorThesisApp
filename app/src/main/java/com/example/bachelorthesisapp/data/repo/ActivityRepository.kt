package com.example.bachelorthesisapp.data.repo

import com.example.bachelorthesisapp.data.datasource.ActivitiesLocalDataSource
import com.example.bachelorthesisapp.data.model.entities.ActivityEntity
import com.example.bachelorthesisapp.data.remote.Resource
import com.example.bachelorthesisapp.data.remote.networkCall
import com.example.bachelorthesisapp.data.remote.toEntity
import com.example.bachelorthesisapp.datasource.RemoteDataSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class ActivityRepository @Inject constructor(
    private val activityLocalDataSource: ActivitiesLocalDataSource,
    private val remoteDataSource: RemoteDataSource,
) {

    private val _activityFlow = MutableSharedFlow<Resource<List<ActivityEntity>>>()
    val activityFlow: Flow<Resource<List<ActivityEntity>>> = _activityFlow

    suspend fun fetchActivity() = networkCall(
        localSource = { activityLocalDataSource.getAllEntities() },
        remoteSource = { remoteDataSource.getActivityData() },
        compareData = { remoteActivity, localActivity ->
            val remoteResponse = listOf(remoteActivity.toEntity())
            if (remoteResponse != localActivity)
                activityLocalDataSource.repopulateEntities(remoteResponse)
        },
        onResult = { activity ->
            _activityFlow.emit(
                when (activity) {
                    is Resource.Error -> Resource.Error(activity.exception)
                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> Resource.Success(listOf(activity.data.toEntity()))
                }
            )
        }
    )
}