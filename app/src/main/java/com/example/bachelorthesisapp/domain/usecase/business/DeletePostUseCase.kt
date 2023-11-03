package com.example.bachelorthesisapp.domain.usecase.business

import com.example.bachelorthesisapp.core.domain.UseCase
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.data.posts.OfferPostsRepository
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.posts.remote.dto.OfferPostDto
import javax.inject.Inject

class DeletePostUseCase @Inject constructor(
    private val postsRepository: OfferPostsRepository
) : UseCase<DeletePostUseCaseArgs, OfferPostDto> {

    override suspend fun execute(
        args: DeletePostUseCaseArgs,
        successCallback: (r: OfferPostDto) -> Unit,
        failureCallback: (e: Exception) -> Unit
    ) {
        when (val resource = postsRepository.deletePost(post = args.post)) {
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

data class DeletePostUseCaseArgs(
    val post: OfferPost
)