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
import com.example.bachelorthesisapp.data.model.entities.AppointmentRequest
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.ClientEntity
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.model.entities.RequestStatus
import com.example.bachelorthesisapp.presentation.ui.theme.SkyGray
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState

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
    contentClients: UiState<List<ClientEntity>> = UiState.Loading
) {
    BackdropScaffold(modifier = Modifier,
        scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
        frontLayerShape = RoundedCornerShape(30.dp),
        frontLayerScrimColor = Color.Unspecified,
        frontLayerBackgroundColor = Color.Gray,
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
        // Front layer contains the list of the business filtered by the business type selected in the back layer
        frontLayerContent = {
            RequestsFrontLayerContent(
                businessId = businessId,
                clientViewModel = clientViewModel,
                contentPosts = contentPosts,
                contentClients = contentClients,
                contentRequests = contentRequests
            )
        })
}