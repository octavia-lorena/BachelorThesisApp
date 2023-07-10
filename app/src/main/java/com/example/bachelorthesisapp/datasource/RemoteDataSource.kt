package com.example.bachelorthesisapp.datasource

import com.example.bachelorthesisapp.data.remote.ActivityDto
import com.example.bachelorthesisapp.data.remote.BusinessDto

interface RemoteDataSource {
    suspend fun getActivityData(): ActivityDto
    suspend fun getBusinessData(): List<BusinessDto>
    suspend fun getBusinessDataByType(type: String): List<BusinessDto>

}