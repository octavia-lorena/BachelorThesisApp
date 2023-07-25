package com.example.bachelorthesisapp.datasource

import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.remote.ActivityDto
import com.example.bachelorthesisapp.data.remote.BusinessDto
import com.example.bachelorthesisapp.data.remote.EventDto
import com.example.bachelorthesisapp.data.remote.OfferPostDto

interface RemoteDataSource {
    suspend fun getActivityData(): ActivityDto
    suspend fun getBusinessData(): List<BusinessDto>
    suspend fun getBusinessDataByType(type: String): List<BusinessDto>
    suspend fun getPostData(): List<OfferPostDto>
    suspend fun getPostDataById(postId: Int): OfferPostDto
    suspend fun getPostDataByBusinessId(businessId: String): List<OfferPostDto>
    suspend fun addPost(post: OfferPost): OfferPostDto
    suspend fun deletePost(id: Int): OfferPostDto
    suspend fun updatePost(id:Int, title:String, description:String, photos:List<String>, price:Int): OfferPostDto

    suspend fun getEventData(): List<EventDto>
    suspend fun getEventDataById(eventId: Int): EventDto

    suspend fun getEventDataByOrganizerId(organizerId: String): List<EventDto>


    suspend fun addEvent(event: Event): EventDto
}