package com.example.bachelorthesisapp.data.remote

import com.example.bachelorthesisapp.data.model.entities.OfferPost
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Api {

    companion object {
        // const val BASE_URL = "https://www.boredapi.com/api/"
        const val BASE_URL = "http://10.0.2.2:3000"

    }

    @GET("activity?")
    suspend fun getActivityData(): ActivityDto

    @GET("/business")
    suspend fun getBusinessesData(): List<BusinessDto>

    @GET("/business/type")
    suspend fun getBusinessesDataByType(type: String): List<BusinessDto>

    @GET("/posts")
    suspend fun getPostsData(): List<OfferPostDto>

    @GET("/post/{id}")
    suspend fun getPostsDataById( @Path(
        value = "id",
        encoded = true
    ) postId: Int): OfferPostDto

    @GET("/posts/{id}")
    suspend fun getPostsDataByBusinessId(
        @Path(
            value = "id",
            encoded = true
        ) businessId: String
    ): List<OfferPostDto>

    @POST("/post")
    suspend fun addPost(@Body post: OfferPost): OfferPostDto

    @DELETE("/post/{id}")
    suspend fun deletePost(
        @Path(
            value = "id",
            encoded = true
        ) id: Int
    ): OfferPostDto

    @FormUrlEncoded
    @PUT("/post/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("photos") photos: List<String>,
        @Field("price") price: Int
    ): OfferPostDto

    @GET("/events")
    suspend fun getEventsData(): List<EventDto>

    @GET("/events/{id}")
    suspend fun getEventDataByOrganizerId(
        @Path(
            value = "id",
            encoded = true
        ) organizerId: String
    ): List<EventDto>

    @GET("/event/{id}")
    suspend fun getEventDataById(
        @Path(
            value = "id",
            encoded = true
        ) eventId: Int
    ): EventDto
}