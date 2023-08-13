package com.example.bachelorthesisapp.data.posts.remote.api

import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.posts.remote.dto.OfferPostDto
import com.example.bachelorthesisapp.domain.model.OfferPostModel
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
    suspend fun getPostsData(): List<OfferPostDto>

    @GET("/post/{id}")
    suspend fun getPostsDataById(
        @Path(
            value = "id",
            encoded = true
        ) postId: Int
    ): OfferPostDto

    @GET("posts/{businessId}")
    suspend fun getPostsByBusinessId(
        @Path(
            value = "businessId",
            encoded = true
        ) businessId: String
    ): List<OfferPostDto>

    @POST("/post")
    suspend fun addPost(@Body post: OfferPostModel): OfferPostDto

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

    @FormUrlEncoded
    @PUT("/post/rate/{id}")
    suspend fun updatePostRating(
        @Path("id") id: Int,
        @Field("ratingValue") ratingValue: Int
    ): OfferPostDto


}