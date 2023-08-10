package com.example.bachelorthesisapp.data.datasource

import android.util.Log
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.remote.NotificationApi
import com.example.bachelorthesisapp.data.remote.OfferPostDto
import com.example.bachelorthesisapp.data.remote.PostApi
import com.example.bachelorthesisapp.datasource.ApiResponse
import com.example.bachelorthesisapp.datasource.PostRemoteDataSource
import com.example.bachelorthesisapp.exception.NetworkCallException
import javax.inject.Inject
import retrofit2.Response

class PostRemoteDataSourceImpl @Inject constructor(
    private val api: PostApi,
    private val notificationApi: NotificationApi,
) : PostRemoteDataSource {

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
            Log.e(PostRemoteDataSourceImpl::class.simpleName, ex.stackTraceToString())
            ApiResponse.Error(exception = ex)
        }


    override suspend fun getAllPost(): List<OfferPostDto> = api.getPostsData()

    override suspend fun getPostById(postId: Int): OfferPostDto = api.getPostsDataById(postId)
    override suspend fun getPostByBusinessId(businessId: String): List<OfferPostDto> =
        api.getPostsByBusinessId(businessId)

    override suspend fun addPost(post: OfferPost): OfferPostDto = api.addPost(post)
    override suspend fun deletePost(id: Int): OfferPostDto = api.deletePost(id)
    override suspend fun updatePost(
        id: Int,
        title: String,
        description: String,
        photos: List<String>,
        price: Int
    ): OfferPostDto = api.updatePost(id, title, description, photos, price)

    override suspend fun ratePost(id: Int, ratingValue: Int): OfferPostDto =
        api.updatePostRating(id, ratingValue)

}