package com.example.bachelorthesisapp.data.repo

import android.util.Log
import com.example.bachelorthesisapp.data.datasource.BusinessLocalDataSource
import com.example.bachelorthesisapp.data.datasource.BusinessRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.datasource.EventRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.datasource.PostRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.datasource.PostsLocalDataSource
import com.example.bachelorthesisapp.data.datasource.RequestRemoteDataSourceImpl
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.remote.Resource
import com.example.bachelorthesisapp.data.remote.networkCall
import com.example.bachelorthesisapp.data.remote.toEntity
import com.example.bachelorthesisapp.data.repo.firebase.PushNotification
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class BusinessRepository @Inject constructor(
    private val businessLocalDataSource: BusinessLocalDataSource,
    private val postsLocalDataSource: PostsLocalDataSource,
    private val businessRemoteDataSource: BusinessRemoteDataSourceImpl,
    private val postRemoteDataSource: PostRemoteDataSourceImpl,
    private val eventRemoteDataSource: EventRemoteDataSourceImpl,
    private val requestRemoteDataSource: RequestRemoteDataSourceImpl
) {

    // BUSINESS FLOW
    private val _businessFlow = MutableSharedFlow<Resource<List<BusinessEntity>>>()
    val businessFlow: Flow<Resource<List<BusinessEntity>>> = _businessFlow

    // POSTS FLOW
    private val _postFlow = MutableSharedFlow<Resource<List<OfferPost>>>()
    val postFlow: Flow<Resource<List<OfferPost>>> = _postFlow

    // POSTS BY BUSINESS ID FLOW
    private val _postBusinessFlow = MutableSharedFlow<Resource<List<OfferPost>>>()
    val postBusinessFlow: Flow<Resource<List<OfferPost>>> = _postBusinessFlow

    // POST BY ID FLOW
    private val _postCurrentFlow = MutableSharedFlow<Resource<OfferPost?>>()
    val postCurrentFlow: Flow<Resource<OfferPost?>> = _postCurrentFlow

    private val _postResultFlow = MutableSharedFlow<Resource<OfferPost>>()
    val postResultFlow: Flow<Resource<OfferPost>> = _postResultFlow

    suspend fun fetchBusinesses() = networkCall(
        localSource = { businessLocalDataSource.getAllEntities() },
        remoteSource = { businessRemoteDataSource.getAllBusiness() },
        compareData = { remoteBusiness, localBusiness ->
            if (remoteBusiness != localBusiness) {
                val businessEntities = remoteBusiness.map { it.toEntity() }
                businessLocalDataSource.repopulateEntities(businessEntities)
            }
        },
        onResult = { activity ->
            _businessFlow.emit(
                when (activity) {
                    is Resource.Error -> Resource.Error(activity.exception)
                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> Resource.Success(activity.data.map { it.toEntity() })
                }
            )
        }
    )

    suspend fun fetchBusinessesByType(type: String) = networkCall(
        localSource = { businessLocalDataSource.getBusinessesByType(type) },
        remoteSource = { businessRemoteDataSource.getAllBusiness() },
        compareData = { remoteBusiness, localBusiness ->
            if (remoteBusiness != localBusiness) {
                val businessEntities = remoteBusiness.map { it.toEntity() }
                businessLocalDataSource.repopulateEntities(businessEntities)
            }
        },
        onResult = { activity ->
            _businessFlow.emit(
                when (activity) {
                    is Resource.Error -> Resource.Error(activity.exception)
                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> Resource.Success(activity.data.map { it.toEntity() })
                }
            )
        }
    )

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
        onResult = {
//            _postFlow.emit(
//                when (activity) {
//                    is Resource.Error -> {
//                        Resource.Error<Exception>(activity.exception)
//                        val posts = postsLocalDataSource.getAllEntities()
//                        Resource.Success(posts)
//                    }
//
//                    is Resource.Loading -> Resource.Loading()
//                    is Resource.Success -> Resource.Success(activity.data.map { it.toEntity() })
//                }
//            )
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

    suspend fun fetchPostById(postId: Int) = networkCall(
        localSource = { postsLocalDataSource.getEntity(postId.toString()) },
        remoteSource = { postRemoteDataSource.getPostById(postId) },
        compareData = { _, _ ->
//            fetchAllPosts()
//            delay(2000L)
        },
        onResult = { post ->
            //fetchAllPosts()
            _postCurrentFlow.emit(
                when (post) {
                    is Resource.Error -> {
                        Resource.Error<Exception>(post.exception)
                        val posts = postsLocalDataSource.getEntity(postId.toString())
                        Resource.Success(posts)
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Success -> Resource.Success(post.data.toEntity())
                }
            )
        }
    )

    suspend fun fetchPostsLocal() {
        val posts = postsLocalDataSource.getAllEntities()
        _postFlow.emit(Resource.Success(posts))
    }

    suspend fun addPost(post: OfferPost) {
        try {
            _postResultFlow.emit(Resource.Loading())
            val result = postRemoteDataSource.addPost(post)
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
            fetchPostsByBusinessId(post.businessId)
            _postResultFlow.emit(Resource.Success(result.toEntity()))
        } catch (e: Exception) {
            e.printStackTrace()
            _postResultFlow.emit(Resource.Error(e))

        }
    }

    suspend fun updatePost(
        id: Int,
        title: String,
        description: String,
        photos: List<String>,
        price: Int
    ) {
        try {
            _postResultFlow.emit(Resource.Loading())
            val result = postRemoteDataSource.updatePost(id, title, description, photos, price)
            _postResultFlow.emit(Resource.Success(result.toEntity()))
        } catch (e: Exception) {
            Log.d("UPDATE_ERROR", e.stackTraceToString())
            _postResultFlow.emit(Resource.Error(e))

        }
    }

    suspend fun setVendorValue(eventId: Int, category: String, postId: Int) {
        try {
            eventRemoteDataSource.setVendorForCategory(eventId, category, postId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun setEventCost(eventId: Int, price: Int){
        try {
            eventRemoteDataSource.setEventCost(eventId = eventId, price = price)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteRequest(requestId: Int) {
        try {
            requestRemoteDataSource.deleteRequest(requestId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteAppointment(requestId: Int) {
        try {
            requestRemoteDataSource.deleteAppointment(requestId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun sendNotification(pushNotification: PushNotification) =
        businessRemoteDataSource.sendNotification(pushNotification)

}