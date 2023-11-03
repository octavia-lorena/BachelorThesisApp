package com.example.bachelorthesisapp.domain.usecase.business

import com.example.bachelorthesisapp.core.domain.UseCase
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.data.appointment_requests.AppointmentRequestsRepository
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.appointment_requests.remote.dto.AppointmentRequestDto
import com.example.bachelorthesisapp.data.model.RequestStatus
import com.example.bachelorthesisapp.data.posts.OfferPostsRepository
import com.example.bachelorthesisapp.data.posts.remote.dto.OfferPostDto
import javax.inject.Inject

class GetAppointmentsByBusinessIdUseCase @Inject constructor(
    private val requestsRepository: AppointmentRequestsRepository
): UseCase<GetAppointmentsByBusinessIdUseCaseArgs, List<AppointmentRequestDto>> {

    override suspend fun execute(
        args: GetAppointmentsByBusinessIdUseCaseArgs,
        successCallback: (r: List<AppointmentRequestDto>) -> Unit,
        failureCallback: (e: Exception) -> Unit
    ) {
        when (val resource = requestsRepository.getRequestsByBusinessId(businessId = args.businessId)){
            is Resource.Success -> {
                successCallback(resource.value.filter { it.status == RequestStatus.Accepted.name })
            }

            is Resource.Error -> {
                failureCallback(resource.cause)
            }
            else -> {}
        }
    }
}

data class GetAppointmentsByBusinessIdUseCaseArgs(
    val businessId: String
)