package com.example.bachelorthesisapp.presentation.ui.components.business

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.entities.AppointmentRequest
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.ClientEntity
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.model.entities.RequestStatus
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.DarkGray
import com.example.bachelorthesisapp.presentation.ui.theme.GrayBlue
import com.example.bachelorthesisapp.presentation.ui.theme.GreenLight
import com.example.bachelorthesisapp.presentation.ui.theme.IrisBlue
import com.example.bachelorthesisapp.presentation.ui.theme.IrisBlueDark
import com.example.bachelorthesisapp.presentation.ui.theme.IrisBlueLight
import com.example.bachelorthesisapp.presentation.ui.theme.RedSoft
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.SkyGray
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.ui.theme.WhiteTransparent
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period
import kotlin.time.Duration.Companion.seconds

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
            Column(verticalArrangement = Arrangement.Top,
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