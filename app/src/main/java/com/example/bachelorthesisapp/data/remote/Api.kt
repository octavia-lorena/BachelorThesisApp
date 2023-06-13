package com.example.bachelorthesisapp.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface Api {

    companion object {
        const val BASE_URL = "https://www.boredapi.com/api/"
//        private const val BASE_URL = "http://10.0.2.2:3000"

    }

    @GET("activity?")
    suspend fun getActivityData(): Response<ActivityDto>
}