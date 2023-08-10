package com.example.bachelorthesisapp.presentation.ui.components.business

import androidx.compose.runtime.Composable
import com.example.bachelorthesisapp.data.model.entities.AppointmentRequest
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.ClientEntity
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.model.entities.RequestStatus
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState

@Composable
fun RequestsScreenContent(
    businessId: String,
    contentRequests: UiState<List<AppointmentRequest>> = UiState.Success(
        listOf(
            AppointmentRequest(
                11, 11, 5, RequestStatus.Pending
            ),
            AppointmentRequest(
                15, 10, 6, RequestStatus.Pending
            )
        )
    ),
    contentBusiness: UiState<BusinessEntity> = UiState.Loading,
    clientViewModel: ClientViewModel,
    contentEvents: UiState<List<Event>> = UiState.Loading,
    contentPosts: UiState<List<OfferPost>> = UiState.Loading,
    contentClients: UiState<List<ClientEntity>> = UiState.Loading

) {
    RequestsScreenBackdrop(
        businessId = businessId,
        contentBusiness = contentBusiness,
        contentClients = contentClients,
        contentRequests = contentRequests,
        contentEvents = contentEvents,
        contentPosts = contentPosts,
        clientViewModel = clientViewModel
    )
}