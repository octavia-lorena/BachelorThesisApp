package com.example.bachelorthesisapp.data.datasource

import android.util.Log
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.model.entities.toEventData
import com.example.bachelorthesisapp.data.remote.ActivityDto
import com.example.bachelorthesisapp.data.remote.Api
import com.example.bachelorthesisapp.data.remote.BusinessDto
import com.example.bachelorthesisapp.data.remote.EventDto
import com.example.bachelorthesisapp.data.remote.OfferPostDto
import com.example.bachelorthesisapp.datasource.ApiResponse
import com.example.bachelorthesisapp.datasource.RemoteDataSource
import com.example.bachelorthesisapp.exception.NetworkCallException
import javax.inject.Inject
import retrofit2.Response

class RemoteDataSourceImpl @Inject constructor(
    private val api: Api
) : RemoteDataSource {

    private suspend fun <T> handleApiResponse(execute: suspend () -> Response<T>): ApiResponse<T> =
        try {
            val response = execute()
            if (response.isSuccessful) {
                response.body()?.let {
                    ApiResponse.Success(it)
                } ?: ApiResponse.SuccessWithoutBody
            } else {
                ApiResponse.Error(NetworkCallException(response.code().toString()))
            }
        } catch (ex: Exception) {
            Log.e(RemoteDataSourceImpl::class.simpleName, ex.stackTraceToString())
            ApiResponse.Error(exception = ex)
        }

    override suspend fun getActivityData(): ActivityDto =
        api.getActivityData()

    override suspend fun getBusinessData(): List<BusinessDto> = api.getBusinessesData()
    override suspend fun getBusinessDataByType(type: String): List<BusinessDto> =
        api.getBusinessesDataByType(type)

    override suspend fun getPostData(): List<OfferPostDto> = api.getPostsData()

    override suspend fun getPostDataById(postId: Int): OfferPostDto = api.getPostsDataById(postId)
    override suspend fun getPostDataByBusinessId(businessId: String): List<OfferPostDto> =
        api.getPostsDataByBusinessId(businessId)

    override suspend fun addPost(post: OfferPost): OfferPostDto = api.addPost(post)
    override suspend fun deletePost(id: Int): OfferPostDto = api.deletePost(id)
    override suspend fun updatePost(
        id: Int,
        title: String,
        description: String,
        photos: List<String>,
        price: Int
    ): OfferPostDto = api.updatePost(id, title, description, photos, price)

    override suspend fun getEventData(): List<EventDto> = api.getEventsData()
    override suspend fun getEventDataById(eventId: Int): EventDto = api.getEventDataById(eventId)

    override suspend fun getEventDataByOrganizerId(organizerId: String): List<EventDto> =
        api.getEventDataByOrganizerId(organizerId)

    override suspend fun addEvent(event: Event): EventDto {
        Log.d("NEW_EVENT", "API: ${event.toEventData()}")
        return api.addEvent(event.toEventData())
    }



}