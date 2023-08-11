package com.example.bachelorthesisapp.data.businesses.remote.api

import com.example.bachelorthesisapp.data.businesses.remote.dto.BusinessDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface BusinessApi {

    @GET("/business")
    suspend fun getBusinessesData(): List<BusinessDto>

    @GET("/business/type/{type}")
    suspend fun getBusinessesDataByType(
        @Path(
            value = "type",
            encoded = true
        ) type: String
    ): List<BusinessDto>

    @GET("/business/city/{city}")
    suspend fun getBusinessesDataByCity(
        @Path(
            value = "city",
            encoded = true
        ) city: String
    ): List<BusinessDto>

    @GET("/business/{id}")
    suspend fun getBusinessDataById(
        @Path(
            value = "id",
            encoded = true
        ) businessId: String
    ): BusinessDto

    @FormUrlEncoded
    @PUT("/business/{id}")
    suspend fun updateBusinessDeviceToken(
        @Path("id") id: String,
        @Field("token") token: String,
    ): BusinessDto

}