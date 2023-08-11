package com.example.bachelorthesisapp.presentation.ui.components.business

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.data.clients.local.entity.ClientEntity
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.domain.model.RequestStatus
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.core.presentation.UiState

@Composable
fun RequestsBackLayerContent(
    contentBusiness: UiState<BusinessEntity> = UiState.Loading,
    clientViewModel: ClientViewModel,
    contentEvents: UiState<List<Event>> = UiState.Loading,
    contentPosts: UiState<List<OfferPost>> = UiState.Loading,
    contentClients: UiState<List<ClientEntity>> = UiState.Loading,
    contentRequests: UiState<List<AppointmentRequest>> = UiState.Success(
        listOf(
            AppointmentRequest(
                11, 11, 5, RequestStatus.Pending
            ),
            AppointmentRequest(
                15, 10, 6, RequestStatus.Pending
            )
        )
    )

) {
    val context = LocalContext.current

    when (contentRequests) {
        is UiState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    backgroundColor = Rose,
                    color = CoralLight,
                )
            }
        }

        is UiState.Success -> {
            Log.d("REQUEST", "SUCCESS")
            LazyColumn(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize(),
            ) {
                val requestsList = contentRequests.value
                if (contentPosts is UiState.Success && contentEvents is UiState.Success && contentClients is UiState.Success) {
                    items(requestsList.size) { index ->
                        val request = requestsList[index]
                        val post = contentPosts.value.first { it.id == request.postId }
                        val event = contentEvents.value.first { it.id == request.eventId }
                        val client = contentClients.value.first { it.id == event.organizerId }
                        Log.d("NEW_REQUEST_CARD", "rid ${request.id}, cid ${client.id}")
                        if (contentBusiness is UiState.Success) {
                            val business = contentBusiness.value
                            BusinessRequestCard(
                                post = post,
                                event = event,
                                client = client,
                                onAcceptRequest = {
                                    clientViewModel.acceptRequest(
                                        request.id,
                                        business,
                                        event,
                                        post,
                                        client.deviceToken!!
                                    )
                                }
                            ) {
                                clientViewModel.declineRequest(
                                    request.id,
                                    business.businessName,
                                    event.name,
                                    client.deviceToken!!
                                )
                            }
                        }
                    }
                } else {
                    Log.d("NEW_REQUEST_CARD", "Error/Loading")

                }

            }
        }

        is UiState.Error -> {
            Toast.makeText(context, "Error loading your requests.", Toast.LENGTH_SHORT).show()
        }
    }
}