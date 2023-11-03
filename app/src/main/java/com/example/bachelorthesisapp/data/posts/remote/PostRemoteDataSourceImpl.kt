package com.example.bachelorthesisapp.data.posts.remote

import com.example.bachelorthesisapp.core.remote.RemoteDataSource
import com.example.bachelorthesisapp.core.resources.RemoteResource
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.posts.remote.api.PostApi
import com.example.bachelorthesisapp.data.posts.remote.dto.OfferPostDto
import com.example.bachelorthesisapp.data.model.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRemoteDataSourceImpl @Inject constructor(
    private val api: PostApi,
) : RemoteDataSource() {

    suspend fun getAllPosts(): RemoteResource<List<OfferPostDto>> {
        return withContext(Dispatchers.IO) {
            makeCall {
                api.getPosts()
            }
        }
    }

    suspend fun getPostById(postId: Int): RemoteResource<OfferPostDto> {
        return withContext(Dispatchers.IO) {
            makeCall {
                api.getPostById(postId)
            }
        }
    }

    //     suspend fun getPostByBusinessId(businessId: String): List<OfferPostDto> =
//        api.getPostsByBusinessId(businessId)
    suspend fun getPostsByBusinessId(businessId: String): RemoteResource<List<OfferPostDto>> {
        return withContext(Dispatchers.IO) {
            makeCall {
                api.getPostsByBusinessId(businessId)
            }
        }
    }

    suspend fun addPost(post: OfferPost): RemoteResource<OfferPostDto> {
        return withContext(Dispatchers.IO) {
            makeCall {
                api.addPost(post.toModel())
            }
        }
    }

    suspend fun deletePost(id: Int): RemoteResource<OfferPostDto> {
        return withContext(Dispatchers.IO) {
            makeCall {
                api.deletePost(id)
            }
        }
    }

    suspend fun updatePost(
        id: Int,
        title: String,
        description: String,
        photos: String,
        price: Int
    ): RemoteResource<OfferPostDto> {
        return withContext(Dispatchers.IO) {
            makeCall {
                api.updatePost(
                    id = id,
                    title = title,
                    description = description,
                    photos = photos,
                    price = price
                )
            }
        }
    }

    suspend fun ratePost(id: Int, ratingValue: Int): OfferPostDto =
        api.updatePostRating(id, ratingValue)

}