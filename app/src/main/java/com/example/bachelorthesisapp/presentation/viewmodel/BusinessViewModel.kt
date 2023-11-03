package com.example.bachelorthesisapp.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.model.Rating
import com.example.bachelorthesisapp.data.model.events.CreatePostEvent
import com.example.bachelorthesisapp.data.model.events.UpdatePostEvent
import com.example.bachelorthesisapp.data.model.states.CreatePostFormState
import com.example.bachelorthesisapp.data.model.states.UpdatePostFormState
import com.example.bachelorthesisapp.data.model.validators.CreatePostFormValidator
import com.example.bachelorthesisapp.data.model.validators.UpdatePostFormValidator
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.data.businesses.BusinessRepository
import com.example.bachelorthesisapp.data.authentication.AuthRepository
import com.example.bachelorthesisapp.data.notifications.NotificationData
import com.example.bachelorthesisapp.data.notifications.PushNotification
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.data.appointment_requests.AppointmentRequestsRepository
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.appointment_requests.remote.dto.toEntity
import com.example.bachelorthesisapp.data.authentication.await
import com.example.bachelorthesisapp.data.events.EventsRepository
import com.example.bachelorthesisapp.data.posts.OfferPostsRepository
import com.example.bachelorthesisapp.data.posts.remote.dto.toEntity
import com.example.bachelorthesisapp.domain.usecase.business.AddPostUseCase
import com.example.bachelorthesisapp.domain.usecase.business.AddPostUseCaseArgs
import com.example.bachelorthesisapp.domain.usecase.business.DeletePostUseCase
import com.example.bachelorthesisapp.domain.usecase.business.DeletePostUseCaseArgs
import com.example.bachelorthesisapp.domain.usecase.business.GetAllPostsUseCase
import com.example.bachelorthesisapp.domain.usecase.business.GetAppointmentsByBusinessIdUseCase
import com.example.bachelorthesisapp.domain.usecase.business.GetAppointmentsByBusinessIdUseCaseArgs
import com.example.bachelorthesisapp.domain.usecase.business.GetPostByIdUseCase
import com.example.bachelorthesisapp.domain.usecase.business.GetPostByIdUseCaseArgs
import com.example.bachelorthesisapp.domain.usecase.business.GetPostsByBusinessIdUseCase
import com.example.bachelorthesisapp.domain.usecase.business.GetPostsByBusinessIdUseCaseArgs
import com.example.bachelorthesisapp.domain.usecase.business.GetRequestsByBusinessIdUseCase
import com.example.bachelorthesisapp.domain.usecase.business.GetRequestsByBusinessIdUseCaseArgs
import com.example.bachelorthesisapp.domain.usecase.business.UpdatePostUseCase
import com.example.bachelorthesisapp.domain.usecase.business.UpdatePostUseCaseArgs
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class BusinessViewModel @Inject constructor(
    private val businessRepository: BusinessRepository,
    private val postsRepository: OfferPostsRepository,
    private val eventsRepository: EventsRepository,
    private val requestsRepository: AppointmentRequestsRepository,
    private val authRepository: AuthRepository,
    private val getPostsByBusinessIdUseCase: GetPostsByBusinessIdUseCase,
    private val getRequestsByBusinessIdUseCase: GetRequestsByBusinessIdUseCase,
    private val getAppointmentsByBusinessIdUseCase: GetAppointmentsByBusinessIdUseCase,
    private val getAllPostsUseCase: GetAllPostsUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val addPostUseCase: AddPostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val updatePostUseCase: UpdatePostUseCase
) : ViewModel() {

    val storageRef = FirebaseStorage.getInstance().getReference("images/posts/")

    // CREATE POST STATE
    var createPostState by mutableStateOf(CreatePostFormState())
    private val createPostValidator: CreatePostFormValidator = CreatePostFormValidator()
    private val validationCreatePostEventChannel = Channel<ValidationEvent>()
    val validationCreatePostEvents = validationCreatePostEventChannel.receiveAsFlow()
    var initialPhotos by mutableStateOf(listOf<String>())

    // UPDATE POST STATE
    var updatePostState by mutableStateOf(UpdatePostFormState())
    private val updatePostValidator: UpdatePostFormValidator = UpdatePostFormValidator()
    private val validationUpdatePostEventChannel = Channel<ValidationEvent>()
    val validationUpdatePostEvents = validationUpdatePostEventChannel.receiveAsFlow()

    // LOADING STATE
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

//    val postResultState: Flow<UiState<OfferPost>> =
//        postsRepository.postResultFlow.map { postResult ->
//            when (postResult) {
//                is Resource.Error -> UiState.Error(postResult.cause)
//                is Resource.Loading -> UiState.Loading
//                is Resource.Success -> UiState.Success(postResult.value)
//            }
//        }

    private val _postsState: MutableStateFlow<UiState<List<OfferPost>>> =
        MutableStateFlow(UiState.Loading)
    val postsState: StateFlow<UiState<List<OfferPost>>> = _postsState

    private val _postsByBusinessIdState: MutableStateFlow<UiState<List<OfferPost>>> =
        MutableStateFlow(UiState.Loading)
    val postsByBusinessIdState: StateFlow<UiState<List<OfferPost>>> = _postsByBusinessIdState

    private val _requestsByBusinessIdState: MutableStateFlow<UiState<List<AppointmentRequest>>> =
        MutableStateFlow(UiState.Loading)
    val requestsByBusinessIdState: StateFlow<UiState<List<AppointmentRequest>>> =
        _requestsByBusinessIdState

    private val _appointmentsByBusinessIdState: MutableStateFlow<UiState<List<AppointmentRequest>>> =
        MutableStateFlow(UiState.Loading)
    val appointmentsByBusinessIdState: StateFlow<UiState<List<AppointmentRequest>>> =
        _appointmentsByBusinessIdState

    private val _postByIdState: MutableStateFlow<UiState<OfferPost>> =
        MutableStateFlow(UiState.Loading)
    val postByIdState: StateFlow<UiState<OfferPost>> = _postByIdState

    private val _postResponseState: MutableStateFlow<UiState<OfferPost>> =
        MutableStateFlow(UiState.Loading)
    val postResponseState: StateFlow<UiState<OfferPost>> = _postResponseState


    fun onCreatePostEvent(event: CreatePostEvent) {
        when (event) {
            is CreatePostEvent.TitleChanged -> {
                createPostState = createPostState.copy(title = event.title)
                validateTitleCreatePostForm()
            }

            is CreatePostEvent.DescriptionChanged -> {
                createPostState = createPostState.copy(description = event.description)
                validateDescriptionCreatePostForm()
            }

            is CreatePostEvent.ImagesChanged -> {
                createPostState = createPostState.copy(images = event.images)
                validateImagesCreatePostForm()
            }

            is CreatePostEvent.PriceChanged -> {
                createPostState = createPostState.copy(price = event.price)
                validatePriceCreatePostForm()
            }

            is CreatePostEvent.Submit -> {
                submitCreatePostForm()
            }

        }
    }

    fun onUpdatePostEvent(event: UpdatePostEvent) {
        when (event) {
            is UpdatePostEvent.TitleChanged -> {
                updatePostState = updatePostState.copy(title = event.title)
                validateTitleUpdatePostForm()
            }

            is UpdatePostEvent.DescriptionChanged -> {
                updatePostState = updatePostState.copy(description = event.description)
                validateDescriptionUpdatePostForm()
            }

            is UpdatePostEvent.ImagesChanged -> {
                updatePostState = updatePostState.copy(images = event.images)
                validateImagesUpdatePostForm()
            }

            is UpdatePostEvent.PriceChanged -> {
                updatePostState = updatePostState.copy(price = event.price)
                validatePriceUpdatePostForm()
            }


            is UpdatePostEvent.Submit -> {
                submitUpdatePostForm()
            }

        }
    }

    fun getAllPosts() {
        viewModelScope.launch {
            getAllPostsUseCase.execute(null,
                { list ->
                    _postsState.value = UiState.Success(list.map { it.toEntity() })
                },
                {
                    _postsState.value = UiState.Error(it)

                })
        }
    }

    fun getPostsByBusinessId(businessId: String) {
        viewModelScope.launch {
            getPostsByBusinessIdUseCase.execute(
                GetPostsByBusinessIdUseCaseArgs(businessId = businessId), { list ->
                    _postsByBusinessIdState.value = UiState.Success(list.map { it.toEntity() })
                },
                {
                    _postsByBusinessIdState.value = UiState.Error(it)

                })
        }
    }

    fun getPostById(postId: Int) {
        viewModelScope.launch {
            getPostByIdUseCase.execute(
                GetPostByIdUseCaseArgs(postId = postId),
                {
                    _postByIdState.value = UiState.Success(it.toEntity())
                },
                {
                    _postByIdState.value = UiState.Error(it)
                }
            )
        }
    }

    fun getRequestsByBusinessId(businessId: String) {
        viewModelScope.launch {
            getRequestsByBusinessIdUseCase.execute(
                GetRequestsByBusinessIdUseCaseArgs(businessId = businessId), { list ->
                    _requestsByBusinessIdState.value = UiState.Success(list.map { it.toEntity() })
                },
                {
                    _requestsByBusinessIdState.value = UiState.Error(it)

                })
        }
    }

    fun getAppointmentsByBusinessId(businessId: String) {
        viewModelScope.launch {
            getAppointmentsByBusinessIdUseCase.execute(
                GetAppointmentsByBusinessIdUseCaseArgs(businessId = businessId), { list ->
                    _appointmentsByBusinessIdState.value =
                        UiState.Success(list.map { it.toEntity() })
                },
                {
                    _appointmentsByBusinessIdState.value = UiState.Error(it)

                })
        }
    }

    private fun validateTitleCreatePostForm() {
        val result = createPostValidator.validateTitle(createPostState.title)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            createPostState = createPostState.copy(
                titleError = result.errorMessage,
            )
            return
        } else {
            createPostState = createPostState.copy(
                titleError = null,
            )
            return
        }
    }

    private fun validateDescriptionCreatePostForm() {
        val result = createPostValidator.validateDescription(createPostState.description)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            createPostState = createPostState.copy(
                descriptionError = result.errorMessage,
            )
            return
        } else {
            createPostState = createPostState.copy(
                descriptionError = null,
            )
            return
        }
    }

    private fun validateImagesCreatePostForm() {
        val result = createPostValidator.validatePhotos(createPostState.images)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            createPostState = createPostState.copy(
                imagesError = result.errorMessage,
            )
            return
        } else {
            createPostState = createPostState.copy(
                imagesError = null,
            )
            return
        }

    }

    private fun validatePriceCreatePostForm() {
        val result = createPostValidator.validatePrice(createPostState.price)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            createPostState = createPostState.copy(
                priceError = result.errorMessage,
            )
            return
        } else {
            createPostState = createPostState.copy(
                priceError = null,
            )
            return
        }
    }

    private fun validateTitleUpdatePostForm() {
        val result = updatePostValidator.validateTitle(updatePostState.title)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            updatePostState = updatePostState.copy(
                titleError = result.errorMessage,
            )
            return
        } else {
            updatePostState = updatePostState.copy(
                titleError = null,
            )
            return
        }
    }

    private fun validateDescriptionUpdatePostForm() {
        val result = updatePostValidator.validateDescription(updatePostState.description)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            updatePostState = updatePostState.copy(
                descriptionError = result.errorMessage,
            )
            return
        } else {
            updatePostState = updatePostState.copy(
                descriptionError = null,
            )
            return
        }
    }

    private fun validateImagesUpdatePostForm() {
        val result = updatePostValidator.validatePhotos(updatePostState.images)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            updatePostState = updatePostState.copy(
                imagesError = result.errorMessage,
            )
            return
        } else {
            updatePostState = updatePostState.copy(
                imagesError = null,
            )
            return
        }

    }

    private fun validatePriceUpdatePostForm() {
        val result = updatePostValidator.validatePrice(updatePostState.price)
        val hasError = listOf(
            result,
        ).any { !it.success }
        if (hasError) {
            updatePostState = updatePostState.copy(
                priceError = result.errorMessage,
            )
            return
        } else {
            updatePostState = updatePostState.copy(
                priceError = null,
            )
            return
        }
    }

    private fun submitCreatePostForm() {
        val titleResult = createPostValidator.validateTitle(createPostState.title)
        val descriptionResult = createPostValidator.validateDescription(createPostState.description)
        val imagesResult = createPostValidator.validatePhotos(createPostState.images)
        val priceResult = createPostValidator.validatePrice(createPostState.price)
        val hasError = listOf(
            titleResult,
            descriptionResult,
            imagesResult,
            priceResult
        ).any { !it.success }
        if (hasError) {
            createPostState = createPostState.copy(
                titleError = titleResult.errorMessage,
                descriptionError = descriptionResult.errorMessage,
                imagesError = imagesResult.errorMessage,
                priceError = priceResult.errorMessage
            )
            return
        }
        val title = createPostState.title
        val description = createPostState.description
        val images = createPostState.images
        val price = createPostState.price

        viewModelScope.launch {
            addPost(title, description, images, price)

        }

    }

    private fun submitUpdatePostForm() {
        val titleResult = updatePostValidator.validateTitle(updatePostState.title)
        val descriptionResult = updatePostValidator.validateDescription(updatePostState.description)
        val imagesResult = updatePostValidator.validatePhotos(updatePostState.images)
        val priceResult = updatePostValidator.validatePrice(updatePostState.price)
        val hasError = listOf(
            titleResult,
            descriptionResult,
            imagesResult,
            priceResult
        ).any { !it.success }
        if (hasError) {
            updatePostState = updatePostState.copy(
                titleError = titleResult.errorMessage,
                descriptionError = descriptionResult.errorMessage,
                imagesError = imagesResult.errorMessage,
                priceError = priceResult.errorMessage
            )
            return
        }
        val id = updatePostState.id
        val title = updatePostState.title
        val description = updatePostState.description
        val images = updatePostState.images
        val price = updatePostState.price

        viewModelScope.launch {
            updatePost(id, title, description, images, price)
        }
    }

    private suspend fun addPost(
        title: String,
        description: String,
        images: String,
        price: String
    ) {
        viewModelScope.launch {
            val businessId = authRepository.currentUser?.uid!!
            val imagesList = images.split(";")
            val pathList = mutableListOf<String>()
            imagesList.forEach {
                val randomNumber = System.currentTimeMillis()
                val path = "$randomNumber.jpeg"
                storageRef.child("$businessId/$path").putFile(Uri.parse(it))
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                            pathList.add(url.toString())
                        }
                    }.await()
                delay(2000L)
            }
            addPostUseCase.execute(
                AddPostUseCaseArgs(
                    OfferPost(
                        0,
                        businessId,
                        title,
                        description,
                        pathList,
                        price.toInt(),
                        Rating(0.0, 0)
                    )
                ),
                {
                    _postResponseState.value = UiState.Success(it.toEntity())
                    clearCreatePostForm()
                    _postResponseState.value = UiState.Loading
                },
                {
                    _postResponseState.value = UiState.Error(it)

                })
        }
    }

    private fun updatePost(
        id: Int,
        title: String,
        description: String,
        images: String,
        price: String
    ) {
        viewModelScope.launch {
            val businessId = authRepository.currentUser?.uid!!
            val pastImages = images.split(";").filter { it in initialPhotos }
            val imagesList = images.split(";").filter { it !in initialPhotos }
            val pathList = mutableListOf<String>()
            imagesList.forEach {
                Log.d("IMAGE", it)
                val randomNumber = System.currentTimeMillis()
                val path = "$randomNumber.jpeg"
                storageRef.child("$businessId/$path").putFile(Uri.parse(it))
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                            pathList.add(url.toString())
                        }
                    }.await()

                delay(2000L)
            }
            pathList.addAll(pastImages)
            updatePostUseCase.execute(
                UpdatePostUseCaseArgs(
                    id = id,
                    title = title,
                    description = description,
                    images = pathList.joinToString(";"),
                    price = price.toInt()
                ),
                {
                    _postResponseState.value = UiState.Success(it.toEntity())
                    clearUpdatePostForm()
                    _postResponseState.value = UiState.Loading
                },
                {
                    _postResponseState.value = UiState.Error(it)
                }
            )

        }
    }

    suspend fun deletePost(post: OfferPost) {
        viewModelScope.launch {
            deletePostUseCase.execute(
                DeletePostUseCaseArgs(post),
                {
                    _postResponseState.value = UiState.Success(it.toEntity())
                },
                {
                    _postResponseState.value = UiState.Error(it)
                }
            )
        }
    }

    fun resetPostResponseState() {
        viewModelScope.launch {
            _postResponseState.value = UiState.Loading
        }
    }

//    fun findPostById(id: Int) {
//        viewModelScope.launch {
//            postsRepository.fetchPostById(id)
//            delay(2000L)
//        }
//    }

    fun setUpdatePostState(post: OfferPost) {
        viewModelScope.launch {
            updatePostState = updatePostState.copy(
                title = post.title,
                description = post.description,
                images = post.images.joinToString(";") { it },
                price = post.price.toString()
            )
            initialPhotos = post.images
            delay(1000L)
        }

    }

    private fun sendNotification(pushNotification: PushNotification) {
        viewModelScope.launch {
            try {
                val response = businessRepository.sendNotification(pushNotification)
                Log.d("TAG", "SUCCESS ${response.raw().body.toString()}")
            } catch (e: Exception) {
                Log.e("TAG", "Error: ${e.stackTraceToString()}")
            }
        }
    }

    fun cancelAppointment(
        requestId: Int,
        business: BusinessEntity,
        event: Event,
        post: OfferPost,
        clientDeviceId: String
    ) {
        viewModelScope.launch {
            // delete request
            requestsRepository.deleteAppointment(requestId)
            // update event - set vendor value for the category to -1, reset the event cost <- cost - post.price
            eventsRepository.setVendorValue(event.id, business.businessType.name, -1)
            eventsRepository.setEventCost(event.id, -post.price)

        }
        sendNotification(
            PushNotification(
                data = NotificationData(
                    "Canceled appointment",
                    "${business.businessName}, ${business.businessType} canceled the appointment for ${event.name}, with ${post.title}"
                ), to = clientDeviceId
            )
        )
    }

    private fun clearCreatePostForm() {
        viewModelScope.launch {
            createPostState = createPostState.copy(
                title = "",
                titleError = null,
                description = "",
                descriptionError = null,
                images = "",
                imagesError = null,
                price = "",
                priceError = null,
                rating = "",
                ratingError = null
            )
        }
    }

    fun clearUpdatePostForm() {
        viewModelScope.launch {
            updatePostState = updatePostState.copy(
                title = "",
                titleError = null,
                description = "",
                descriptionError = null,
                images = "",
                imagesError = null,
                price = "",
                priceError = null,
                rating = "",
                ratingError = null
            )
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
        object Failure : ValidationEvent()
    }
}