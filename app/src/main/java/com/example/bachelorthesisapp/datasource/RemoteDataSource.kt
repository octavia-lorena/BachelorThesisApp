package com.example.bachelorthesisapp.datasource

import com.example.bachelorthesisapp.data.remote.ActivityDto

interface RemoteDataSource {
    suspend fun getActivityData(): ApiResponse<ActivityDto>
}