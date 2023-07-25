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
import com.example.bachelorthesisapp.presentation.ui.components.BusinessSecondaryAppBar
import com.example.bachelorthesisapp.presentation.ui.components.EventDetailsBackdrop
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun EventDetailsScreen(
    eventId: Int, clientViewModel: ClientViewModel, navHostController: NavHostController
) {

    val currentEvent =
        clientViewModel.eventCurrentState.collectAsStateWithLifecycle(initialValue = UiState.Loading)

    val businessList by
        clientViewModel.businessesState.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    val loadingState by clientViewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = loadingState)

    var businessType by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = businessType) {
        clientViewModel.findEventById(eventId)
        if (businessType == "") {
            Log.d("FILTER_VENDORS", "LEe $businessType")
            clientViewModel.loadBusinesses()
        } else {
            clientViewModel.findBusinessesByType(businessType)
            Log.d("FILTER_VENDORS", "LE $businessType")

        }
    }

    when (currentEvent.value) {
        is UiState.Loading -> {
            CircularProgressIndicator(backgroundColor = Rose, color = CoralLight)
        }

        is UiState.Success -> {
            val event = (currentEvent.value as UiState.Success<Event>).value
            Scaffold(
                topBar = {
                    BusinessSecondaryAppBar(
                        title = event.name, navController = navHostController
                    )
                }, drawerGesturesEnabled = true, backgroundColor = Color.White
            ) { innerPadding ->
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = {
                        clientViewModel.findEventById(eventId)
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = innerPadding.calculateBottomPadding(), top = 10.dp)
                    ) {
                        EventDetailsScreenContent(
                            event = event,
                            businessState = businessList,
                            onBusinessTypeFilterClick = { type ->
                                Log.d("FILTER_VENDORS", type)
                                businessType = type
                                clientViewModel.findBusinessesByType(type)
                            },
                            onBusinessTypePostClick = {}
                        )
                    }
                }

            }
        }

        is UiState.Error -> {}
    }


}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Preview(showSystemUi = true)
@Composable
fun EventDetailsScreenContent(
    event: Event = Event(
        1,
        "",
        "A+B Wedding",
        "A classy wedding, bla bla, family and friends, love and marriage bla bla",
        EventType.Wedding,
        LocalDate.parse("2023-07-25"),
        "13:30",
        100,
        1000,
        500,
        mapOf(
            Pair(BusinessType.Beauty, -1),
            Pair(BusinessType.CakeShop, 2),
            Pair(BusinessType.Florist, -1),
            Pair(BusinessType.Venue, -1),
            Pair(BusinessType.DecorDesign, 2),
            Pair(BusinessType.Entertainment, -1),
            Pair(BusinessType.Musician, -1),
            Pair(BusinessType.PhotoVideo, 2)
        ),
        EventStatus.Planning
    ),
    businessState: UiState<List<BusinessEntity>> = UiState.Loading,
    onBusinessTypeFilterClick: (String) -> Unit = {},
    onBusinessTypePostClick: () -> Unit = {},
) {
    EventDetailsBackdrop(
        event = event, onBusinessTypeFilterClick = onBusinessTypeFilterClick,
        businessState = businessState
    )
}