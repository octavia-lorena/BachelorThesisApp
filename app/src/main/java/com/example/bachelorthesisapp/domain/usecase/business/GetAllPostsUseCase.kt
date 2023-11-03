package com.example.bachelorthesisapp.domain.usecase.business

import com.example.bachelorthesisapp.core.domain.UseCase
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.data.posts.OfferPostsRepository
import com.example.bachelorthesisapp.data.posts.remote.dto.OfferPostDto
import javax.inject.Inject

class GetAllPostsUseCase @Inject constructor(
    private val postsRepository: OfferPostsRepository
): UseCase<Any?, List<OfferPostDto>> {

    override suspend fun execute(
        args: Any?,
        successCallback: (r: List<OfferPostDto>) -> Unit,
        failureCallback: (e: Exception) -> Unit
    ) {
        when (val resource = postsRepository.getAllPosts()){
            is Resource.Success -> {
                successCallback(resource.value)
            }

            is Resource.Error -> {
                failureCallback(resource.cause)
            }
            else -> {}
        }
    }
}