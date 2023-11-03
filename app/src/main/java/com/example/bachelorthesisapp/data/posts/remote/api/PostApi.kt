package com.example.bachelorthesisapp.data.posts.remote.api

import com.example.bachelorthesisapp.data.posts.remote.dto.OfferPostDto
import com.example.bachelorthesisapp.data.model.OfferPostModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostApi {
    @GET("/posts")
    suspend fun getPosts(): Response<List<OfferPostDto>>

    @GET("/post/{id}")
    suspend fun getPostById(
        @Path(
            value = "id",
            encoded = true
        ) postId: Int
    ): Response<OfferPostDto>

    @GET("posts/{businessId}")
    suspend fun getPostsByBusinessId(
        @Path(
            value = "businessId",
            encoded = true
        ) businessId: String
    ): Response<List<OfferPostDto>>

    @POST("/post")
    suspend fun addPost(@Body post: OfferPostModel): Response<OfferPostDto>

    @DELETE("/post/{id}")
    suspend fun deletePost(
        @Path(
            value = "id",
            encoded = true
        ) id: Int
    ): Response<OfferPostDto>

    @FormUrlEncoded
    @PUT("/post/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("photos") photos: String,
        @Field("price") price: Int
    ): Response<OfferPostDto>

    @FormUrlEncoded
    @PUT("/post/rate/{id}")
    suspend fun updatePostRating(
        @Path("id") id: Int,
        @Field("ratingValue") ratingValue: Int
    ): OfferPostDto


}