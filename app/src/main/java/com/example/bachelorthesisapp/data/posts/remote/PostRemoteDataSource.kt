package com.example.bachelorthesisapp.data.posts.remote

import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.posts.remote.dto.OfferPostDto

interface PostRemoteDataSource {
    suspend fun getAllPost(): List<OfferPostDto>
    suspend fun getPostById(postId: Int): OfferPostDto
    suspend fun getPostByBusinessId(businessId: String): List<OfferPostDto>
    suspend fun addPost(post: OfferPost): OfferPostDto
    suspend fun deletePost(id: Int): OfferPostDto
    suspend fun updatePost(
        id: Int,
        title: String,
        description: String,
        photos: String,
        price: Int
    ): OfferPostDto
    suspend fun ratePost(
        id: Int,
        ratingValue: Int
    ): OfferPostDto

}