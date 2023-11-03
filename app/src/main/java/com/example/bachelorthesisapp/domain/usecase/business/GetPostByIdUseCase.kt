package com.example.bachelorthesisapp.domain.usecase.business

import com.example.bachelorthesisapp.core.domain.UseCase
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.data.posts.OfferPostsRepository
import com.example.bachelorthesisapp.data.posts.remote.dto.OfferPostDto
import javax.inject.Inject

class GetPostByIdUseCase @Inject constructor(
    private val postsRepository: OfferPostsRepository
): UseCase<GetPostByIdUseCaseArgs, OfferPostDto> {

    override suspend fun execute(
        args: GetPostByIdUseCaseArgs,
        successCallback: (r: OfferPostDto) -> Unit,
        failureCallback: (e: Exception) -> Unit
    ) {
        when (val resource = postsRepository.getPostById(postId = args.postId)){
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

data class GetPostByIdUseCaseArgs(
    val postId: Int
)