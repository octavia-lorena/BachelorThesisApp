package com.example.bachelorthesisapp.data.posts.remote

import android.util.Log
import com.example.bachelorthesisapp.data.notifications.NotificationApi
import com.example.bachelorthesisapp.core.remote.ApiResponse
import com.example.bachelorthesisapp.core.remote.NetworkCallException
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.posts.remote.api.PostApi
import com.example.bachelorthesisapp.data.posts.remote.dto.OfferPostDto
import com.example.bachelorthesisapp.data.model.toModel
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

    override suspend fun addPost(post: OfferPost): OfferPostDto = api.addPost(post.toModel())
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