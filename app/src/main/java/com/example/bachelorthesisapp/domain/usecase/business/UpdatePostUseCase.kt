package com.example.bachelorthesisapp.domain.usecase.business

import com.example.bachelorthesisapp.core.domain.UseCase
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.data.posts.OfferPostsRepository
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.posts.remote.dto.OfferPostDto
import javax.inject.Inject

class UpdatePostUseCase @Inject constructor(
    private val postsRepository: OfferPostsRepository
) : UseCase<UpdatePostUseCaseArgs, OfferPostDto> {

    override suspend fun execute(
        args: UpdatePostUseCaseArgs,
        successCallback: (r: OfferPostDto) -> Unit,
        failureCallback: (e: Exception) -> Unit
    ) {
        when (val resource = postsRepository.updatePost(
            id = args.id,
            title = args.title,
            description = args.description,
            price = args.price,
            photos = args.images
        )) {
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

data class UpdatePostUseCaseArgs(
    val id: Int,
    val title: String,
    val description: String,
    val images: String,
    val price: Int
)