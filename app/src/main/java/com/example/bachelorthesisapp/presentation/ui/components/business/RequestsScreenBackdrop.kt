package com.example.bachelorthesisapp.presentation.ui.components.business

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.data.clients.local.entity.ClientEntity
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.model.RequestStatus
import com.example.bachelorthesisapp.presentation.ui.theme.OffWhite
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RequestsScreenBackdrop(
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
    contentClients: UiState<List<ClientEntity>> = UiState.Loading,
    contentAppointments: UiState<List<AppointmentRequest>> = UiState.Loading

) {
    BackdropScaffold(
        modifier = Modifier,
        scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
        frontLayerShape = RoundedCornerShape(30.dp),
        frontLayerScrimColor = Color.Unspecified,
        frontLayerBackgroundColor = OffWhite,
        backLayerBackgroundColor = Color.White,
        peekHeight = 50.dp,
        stickyFrontLayer = false,
        frontLayerElevation = 20.dp,
        headerHeight = 200.dp,
        // Back layer displays the event details info and the business type selection mechanism
        backLayerContent = {
            RequestsBackLayerContent(
                contentPosts = contentPosts,
                contentRequests = contentRequests,
                contentEvents = contentEvents,
                contentClients = contentClients,
                contentBusiness = contentBusiness,
                clientViewModel = clientViewModel
            )
        },
        appBar = {},
        // Front layer contains the list of the business filtered by the business type and city, selected in the back layer
        frontLayerContent = {
            RequestsFrontLayerContent(
                businessId = businessId,
                clientViewModel = clientViewModel,
                contentPosts = contentPosts,
                contentClients = contentClients,
                contentRequests = contentAppointments
            )
        })
}