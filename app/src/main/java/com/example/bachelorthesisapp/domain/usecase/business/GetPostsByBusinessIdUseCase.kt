package com.example.bachelorthesisapp.domain.usecase.business

import com.example.bachelorthesisapp.core.domain.UseCase
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.data.posts.OfferPostsRepository
import com.example.bachelorthesisapp.data.posts.remote.dto.OfferPostDto
import javax.inject.Inject

class GetPostsByBusinessIdUseCase @Inject constructor(
    private val postsRepository: OfferPostsRepository
): UseCase<GetPostsByBusinessIdUseCaseArgs, List<OfferPostDto>> {

    override suspend fun execute(
        args: GetPostsByBusinessIdUseCaseArgs,
        successCallback: (r: List<OfferPostDto>) -> Unit,
        failureCallback: (e: Exception) -> Unit
    ) {
        when (val resource = postsRepository.getPostsByBusinessId(businessId = args.businessId)){
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

data class GetPostsByBusinessIdUseCaseArgs(
    val businessId: String
)