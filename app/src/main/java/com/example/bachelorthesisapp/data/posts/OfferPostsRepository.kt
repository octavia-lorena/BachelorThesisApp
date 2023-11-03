package com.example.bachelorthesisapp.data.posts

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.bachelorthesisapp.core.remote.networkCall
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.core.resources.toResource
import com.example.bachelorthesisapp.data.posts.local.PostsLocalDataSource
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.posts.remote.PostRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.posts.remote.dto.OfferPostDto
import com.example.bachelorthesisapp.data.posts.remote.dto.toEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class OfferPostsRepository @Inject constructor(
    private val postRemoteDataSource: PostRemoteDataSourceImpl,
    private val postsLocalDataSource: PostsLocalDataSource,
) {

    // POSTS BY BUSINESS ID FLOW
    private val _postBusinessFlow = MutableSharedFlow<Resource<List<OfferPost>>>()
    val postBusinessFlow: Flow<Resource<List<OfferPost>>> = _postBusinessFlow

    // POST BY ID FLOW
//    private val _postFlow: MutableState<Resource<OfferPost>> = mutableStateOf(Resource.Loading())
//    val postFlow: State<Resource<OfferPost>> = _postFlow

    private val _postResultFlow = MutableSharedFlow<Resource<OfferPost>>()
    val postResultFlow: Flow<Resource<OfferPost>> = _postResultFlow

    private val _postCurrentFlow = MutableSharedFlow<Resource<OfferPost>>()
    val postCurrentFlow: Flow<Resource<OfferPost>> = _postCurrentFlow

    suspend fun deleteAllPosts() {
        postsLocalDataSource.deleteAllEntities()
    }

    suspend fun ratePost(postId: Int, ratingValue: Int) {
        try {
            postRemoteDataSource.ratePost(postId, ratingValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updatePost(
        id: Int,
        title: String,
        description: String,
        photos: String,
        price: Int
    ): Resource<OfferPostDto> {
        return postRemoteDataSource.updatePost(
            id = id,
            title = title,
            description = description,
            photos = photos,
            price = price
        ).toResource()
    }

    suspend fun addPost(post: OfferPost): Resource<OfferPostDto> {
        return postRemoteDataSource.addPost(post).toResource()
    }

    suspend fun deletePost(post: OfferPost): Resource<OfferPostDto> {
        return postRemoteDataSource.deletePost(post.id).toResource()
    }

    suspend fun getAllPosts(): Resource<List<OfferPostDto>> {
        return postRemoteDataSource.getAllPosts().toResource()
    }

    suspend fun getPostsByBusinessId(businessId: String): Resource<List<OfferPostDto>> {
        return postRemoteDataSource.getPostsByBusinessId(businessId).toResource()
    }

    suspend fun getPostById(postId: Int): Resource<OfferPostDto> {
        return postRemoteDataSource.getPostById(postId).toResource()
    }


}