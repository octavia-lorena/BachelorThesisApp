package com.example.bachelorthesisapp.presentation.ui.screen.client

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.BusinessType
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.EventStatus
import com.example.bachelorthesisapp.data.model.entities.EventType
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.remote.Resource
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessSecondaryAppBar
import com.example.bachelorthesisapp.presentation.ui.components.client.EventDetailsBackdrop
import com.example.bachelorthesisapp.presentation.ui.components.client.EventDetailsScreenContent
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.IrisBlueDark
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun EventDetailsScreen(
    eventId: Int, clientViewModel: ClientViewModel, navHostController: NavHostController
) {

    val currentEvent by remember {
        clientViewModel.eventCurrentState
    }
    val businessList by
    clientViewModel.businessesByTypeState.collectAsStateWithLifecycle(
        initialValue = UiState.Success(
            emptyList()
        )
    )
    val postsState by clientViewModel.postsState.collectAsStateWithLifecycle(
        initialValue = UiState.Loading
    )
    val loadingState by clientViewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = loadingState)

    var businessType by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = null) {
        clientViewModel.findEventById(eventId)
        clientViewModel.loadAllPosts()
    }

    LaunchedEffect(key1 = businessType, key2 = currentEvent) {
        if (businessType == "") {
            Log.d("FILTER_VENDORS", "LEe $businessType")
            clientViewModel.loadBusinesses()
        } else {
            clientViewModel.findBusinessesByType(businessType)
            Log.d("FILTER_VENDORS", "LE $businessType")

        }
    }

    when (currentEvent) {
        is Resource.Loading -> {
            CircularProgressIndicator(backgroundColor = Rose, color = CoralLight)
        }

        is Resource.Success -> {
            val event = (currentEvent as Resource.Success<Event>).data
            LaunchedEffect(Unit) {
                clientViewModel.setUpdateEventState(event)
            }
            Scaffold(
                topBar = {
                    BusinessSecondaryAppBar(
                        title = event.name,
                        navController = navHostController,
                        backgroundColor = IrisBlueDark,
                        elevation = 0.dp
                    )
                }, drawerGesturesEnabled = true, backgroundColor = Color.White
            ) { innerPadding ->
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = {
                        scope.launch {
                            clientViewModel.findEventById(eventId)
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = innerPadding.calculateBottomPadding(), top = 0.dp)
                    ) {
                        if (postsState is UiState.Success<List<OfferPost>>) {
                            EventDetailsScreenContent(
                                event = event,
                                businessState = businessList,
                                onBusinessTypeFilterClick = { type ->
                                    Log.d("FILTER_VENDORS", type)
                                    businessType = type
                                    clientViewModel.findBusinessesByType(type)
                                },
                                onBusinessTypePostClick = {},
                                onBusinessClick = { businessId -> navHostController.navigate("business_profile/$businessId/$eventId") },
                                onCityClicked = { city -> clientViewModel.findBusinessesByCity(city) },
                                postsList = (postsState as UiState.Success<List<OfferPost>>).value,
                                onEditClick = { eventId -> navHostController.navigate("update_event/$eventId") },
                                onPublishClick = { eventId -> clientViewModel.publishEvent(eventId) }
                            )
                        }

                    }
                }

            }
        }

        is Resource.Error -> {}
    }
}