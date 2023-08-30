package com.example.bachelorthesisapp.data.posts

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.bachelorthesisapp.core.remote.networkCall
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.data.posts.local.PostsLocalDataSource
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.posts.remote.PostRemoteDataSourceImpl
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
//    private val _postFlow = MutableSharedFlow<Resource<OfferPost>>()
//    val postFlow: Flow<Resource<OfferPost>> = _postFlow
    private val _postFlow: MutableState<Resource<OfferPost>> = mutableStateOf(Resource.Loading())
    val postFlow: State<Resource<OfferPost>> = _postFlow

    // ALL POSTS FLOW
    private val _postsFlow = MutableSharedFlow<Resource<List<OfferPost>>>()
    val postsFlow: Flow<Resource<List<OfferPost>>> = _postsFlow

    private val _postResultFlow = MutableSharedFlow<Resource<OfferPost>>()
    val postResultFlow: Flow<Resource<OfferPost>> = _postResultFlow

    private val _postCurrentFlow = MutableSharedFlow<Resource<OfferPost>>()
    val postCurrentFlow: Flow<Resource<OfferPost>> = _postCurrentFlow


    suspend fun fetchAllPosts() = networkCall(
        localSource = { postsLocalDataSource.getAllEntities() },
        remoteSource = { postRemoteDataSource.getAllPost() },
        compareData = { remoteBusiness, localBusiness ->
            if (remoteBusiness != localBusiness) {
                Log.d("POSTS", "data updates")
                val businessEntities = remoteBusiness.map { it.toEntity() }
                postsLocalDataSource.repopulateEntities(businessEntities)
            }
        },
        onResult = { posts ->
            _postsFlow.emit(
                when (posts) {
                    is Resource.Success -> {
                        Resource.Success(posts.data.map { it.toEntity() })
                    }

                    is Resource.Loading -> {
                        Resource.Loading()
                    }

                    is Resource.Error -> {
                        Resource.Error<Exception>(posts.exception)
                        val postsLocal =
                            postsLocalDataSource.getAllEntities()
                        Resource.Success(postsLocal)
                    }
                }
            )
        }
    )

    suspend fun fetchPostById(id: Int) = networkCall(
        localSource = { postsLocalDataSource.getEntity(id.toString()) },
        remoteSource = { postRemoteDataSource.getPostById(id) },
        compareData = { _, _ ->
            fetchAllPosts()
            delay(5000L)
        },
        onResult = { post ->
            when (post) {
                is Resource.Error -> {
                    _postFlow.value = Resource.Error(post.exception)
                    val postLocal = postsLocalDataSource.getEntity(id.toString())!!
                    _postFlow.value = Resource.Success(postLocal)
                }

                is Resource.Loading -> _postFlow.value = Resource.Loading()
                is Resource.Success -> _postFlow.value = Resource.Success(post.data.toEntity())
            }


        }
    )

    suspend fun fetchPostsByBusinessId(businessId: String) = networkCall(
        localSource = { postsLocalDataSource.getPostsByBusinessId(businessId) },
        remoteSource = { postRemoteDataSource.getPostByBusinessId(businessId) },
        compareData = { _, _ ->
            fetchAllPosts()
        },
        onResult = { activity ->
            //fetchAllPosts()
            _postBusinessFlow.emit(
                when (activity) {
                    is Resource.Error -> {
                        Resource.Error<Exception>(activity.exception)
                        val posts = postsLocalDataSource.getPostsByBusinessId(businessId)
                        Resource.Success(posts)
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> Resource.Success(activity.data.map { it.toEntity() })
                }
            )
        }
    )

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
    ) {
        try {
            _postResultFlow.emit(Resource.Loading())
            val result = postRemoteDataSource.updatePost(
                id = id,
                title = title,
                description = description,
                photos = photos,
                price = price
            )
            _postResultFlow.emit(Resource.Success(result.toEntity()))
        } catch (e: Exception) {
            Log.d("UPDATE_ERROR", e.stackTraceToString())
            _postResultFlow.emit(Resource.Error(e))

        }
    }

    suspend fun addPost(post: OfferPost) {
        try {
            //_postResultFlow.emit(Resource.Loading())
            val result = postRemoteDataSource.addPost(post)
            fetchAllPosts()
            _postResultFlow.emit(Resource.Success(result.toEntity()))
        } catch (e: Exception) {
            e.printStackTrace()
            _postResultFlow.emit(Resource.Error(e))

        }
    }

    suspend fun deletePost(post: OfferPost) {
        try {
            _postResultFlow.emit(Resource.Loading())
            val result = postRemoteDataSource.deletePost(post.id)
           // fetchAllPosts()
           fetchPostsByBusinessId(post.businessId)
            _postResultFlow.emit(Resource.Success(result.toEntity()))
        } catch (e: Exception) {
            e.printStackTrace()
            _postResultFlow.emit(Resource.Error(e))

        }
    }

//    suspend fun fetchPostsLocal() {
//        val posts = postsLocalDataSource.getAllEntities()
//        _postsFlow.emit(Resource.Success(posts))
//    }


}