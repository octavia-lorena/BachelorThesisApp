package com.example.bachelorthesisapp.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.collectAsState
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
import com.example.bachelorthesisapp.data.authentication.await
import com.example.bachelorthesisapp.data.events.EventsRepository
import com.example.bachelorthesisapp.data.posts.OfferPostsRepository
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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
    private val authRepository: AuthRepository
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


    val postState: Flow<UiState<List<OfferPost>>> =
        postsRepository.postsFlow.map { postEntities ->
            when (postEntities) {
                is Resource.Error -> UiState.Error(postEntities.exception)
                is Resource.Loading -> UiState.Loading
                is Resource.Success -> UiState.Success(postEntities.data)
            }
        }

    val postState1 = postsRepository.postFlow


    val postBusinessState: Flow<UiState<List<OfferPost>>> =
        postsRepository.postBusinessFlow.map { postEntities ->
            when (postEntities) {
                is Resource.Error -> {
                    _isLoading.value = false
                    UiState.Error(postEntities.exception)
                }
                is Resource.Loading -> {
                    _isLoading.value = true
                    UiState.Loading
                }
                is Resource.Success -> {
                    _isLoading.value = false
                    UiState.Success(postEntities.data)
                }
            }
        }

    val postResultState: Flow<UiState<OfferPost>> =
        postsRepository.postResultFlow.map { postResult ->
            when (postResult) {
                is Resource.Error -> UiState.Error(postResult.exception)
                is Resource.Loading -> UiState.Loading
                is Resource.Success -> UiState.Success(postResult.data)
            }
        }

    val postCurrentState: Flow<UiState<OfferPost>> =
        postsRepository.postCurrentFlow.map { postResult ->
            when (postResult) {
                is Resource.Error -> UiState.Error(postResult.exception)
                is Resource.Loading -> UiState.Loading
                is Resource.Success -> UiState.Success(postResult.data!!)
            }
        }

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


    fun loadPostData() {
        viewModelScope.launch {
            val businessId = authRepository.currentUser?.uid!!
            postsRepository.fetchPostsByBusinessId(businessId)
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
            delay(2000L)
            postResultState.collect {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        validationCreatePostEventChannel.send(ValidationEvent.Success)
                    }

                    is UiState.Error -> {
                        validationCreatePostEventChannel.send(ValidationEvent.Failure)
                    }
                }
            }
            createPostState = createPostState.copy(
                title = "",
                titleError = null,
                description = "",
                descriptionError = null,
                images = "",
                imagesError = null,
                price = "",
                priceError = null
            )
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
            delay(2000L)
            postResultState.collect {
                when (it) {
                    is UiState.Loading -> {}
                    is UiState.Success -> {
                        validationUpdatePostEventChannel.send(ValidationEvent.Success)
                    }

                    is UiState.Error -> {
                        validationUpdatePostEventChannel.send(ValidationEvent.Failure)
                    }
                }
            }
            updatePostState = updatePostState.copy(
                title = "",
                titleError = null,
                description = "",
                descriptionError = null,
                images = "",
                imagesError = null,
                price = "",
                priceError = null
            )
        }
    }

    private fun addPost(
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
                // pathList.add(path)
                storageRef.child("$businessId/$path").putFile(Uri.parse(it))
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                            pathList.add(url.toString())
                        }
                    }.await()
                delay(2000L)
            }
            val post = OfferPost(
                0,
                businessId,
                title,
                description,
                pathList,
                price.toInt(),
                Rating(0.0, 0)
            )
            postsRepository.addPost(post)

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
                Log.d("IMAGEE", it)
                val randomNumber = System.currentTimeMillis()
                val path = "$randomNumber.jpeg"
                // pathList.add(path)
                storageRef.child("$businessId/$path").putFile(Uri.parse(it))
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url ->
                            pathList.add(url.toString())
                        }
                    }.await()

                delay(2000L)
            }
            pathList.addAll(pastImages)
            postsRepository.updatePost(
                id,
                title,
                description,
                pathList.joinToString(";"),
                price.toInt()
            )
        }
    }

    fun deletePost(post: OfferPost) {
        viewModelScope.launch {
            postsRepository.deletePost(post)
            delay(2000L)
        }
    }

    fun findPostById(id: Int) {
        viewModelScope.launch {
            postsRepository.fetchPostById(id)
            delay(2000L)
        }
    }

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

    fun clearCreatePostForm() {
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