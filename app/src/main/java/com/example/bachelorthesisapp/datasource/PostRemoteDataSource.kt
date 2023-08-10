package com.example.bachelorthesisapp.datasource

import com.example.bachelorthesisapp.data.model.entities.AppointmentRequest
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.remote.AppointmentRequestDto
import com.example.bachelorthesisapp.data.remote.BusinessDto
import com.example.bachelorthesisapp.data.remote.EventDto
import com.example.bachelorthesisapp.data.remote.OfferPostDto

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
        photos: List<String>,
        price: Int
    ): OfferPostDto
    suspend fun ratePost(
        id: Int,
        ratingValue: Int
    ): OfferPostDto

}