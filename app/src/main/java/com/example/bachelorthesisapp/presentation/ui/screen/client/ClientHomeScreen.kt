package com.example.bachelorthesisapp.presentation.ui.screen.client

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.model.EventStatus
import com.example.bachelorthesisapp.data.model.EventType
import com.example.bachelorthesisapp.presentation.ui.components.common.BottomNavigationBarClient
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessHomeAppBar
import com.example.bachelorthesisapp.presentation.ui.components.business.ClientDrawerContent
import com.example.bachelorthesisapp.presentation.ui.components.common.CreateEventFloatingButton
import com.example.bachelorthesisapp.presentation.ui.components.client.PastEventsCard
import com.example.bachelorthesisapp.presentation.ui.components.client.PlanningEventsCard
import com.example.bachelorthesisapp.presentation.ui.components.client.TodayEventsCard
import com.example.bachelorthesisapp.presentation.ui.components.client.UpcomingEventsCard
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.core.presentation.UiState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun ClientHomeScreen(
    uid: String,
    authViewModel: AuthViewModel,
    clientViewModel: ClientViewModel,
    navHostController: NavHostController,
    askNotificationPermissionCall: () -> Unit
) {
    askNotificationPermissionCall()
    authViewModel.subscribeToTopic(uid)
    val eventState =
        clientViewModel.eventState.collectAsStateWithLifecycle(UiState.Loading)
    val eventPlanningState =
        clientViewModel.eventPlanningState.collectAsStateWithLifecycle(UiState.Loading)
    val eventUpcomingState =
        clientViewModel.eventUpcomingState.collectAsStateWithLifecycle(UiState.Loading)
    val eventTodayState =
        clientViewModel.eventTodayState.collectAsStateWithLifecycle(UiState.Loading)
    val eventPastState =
        clientViewModel.eventPastState.collectAsStateWithLifecycle(UiState.Loading)

    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = context) {
        clientViewModel.loadAllEventsByOrganizerId()
        clientViewModel.clearUpdateEventState()
        clientViewModel.clearCreateEventState()
    }

    val loadingState by clientViewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = loadingState)


    Scaffold(
        topBar = {
            BusinessHomeAppBar(
                title = "Welcome, ${authViewModel.currentUser?.displayName}",
                scaffoldState = scaffoldState
            )
        },
        bottomBar = {
            BottomNavigationBarClient(
                navController = navHostController,
                onItemClick = {
                    var route = it.route
                    val baseRoute = route.split("/")[0]
                    route = "$baseRoute/$uid"
                    navHostController.navigate(route)
                })
        },
        floatingActionButton = {
            CreateEventFloatingButton(navHostController = navHostController, uid = uid)
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            ClientDrawerContent(
                authVM = authViewModel,
                navController = navHostController
            )
        },
        drawerGesturesEnabled = true,
        backgroundColor = Color.White
    ) { innerPadding ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                scope.launch {
                    clientViewModel.loadAllEventsByOrganizerId()
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding(), top = 25.dp)
            ) {
                ClientHomeScreenContent(
                    contentEventsUpcoming = eventUpcomingState.value,
                    contentEventsToday = eventTodayState.value,
                    contentEventsPlanning = eventPlanningState.value,
                    contentEventsPast = eventPastState.value,
                    onUpcomingCardClick = { navHostController.navigate("events/$uid") },
                    onPlanningCardClick = { navHostController.navigate("events/$uid") }
                )
            }
        }
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun ClientHomeScreenContent(
    contentEventsUpcoming: UiState<List<Event>> = UiState.Success(
        listOf(
            Event(
                1,
                "",
                "A+B's wedding",
                "Wedd",
                EventType.Wedding,
                LocalDate.parse("2023-07-22"),
                "15:30",
                200,
                1000,
                0,
                mapOf(),
                EventStatus.Upcoming
            ),
            Event(
                1,
                "",
                "A+B's wedding",
                "Wedd",
                EventType.Wedding,
                LocalDate.parse("2023-08-19"),
                "15:30",
                200,
                1000,
                0,
                mapOf(),
                EventStatus.Upcoming
            ),
            Event(
                1,
                "",
                "A+B's wedding",
                "Wedd",
                EventType.Wedding,
                LocalDate.parse("2024-08-22"),
                "15:30",
                200,
                1000,
                0,
                mapOf(),
                EventStatus.Upcoming
            ), Event(
                1,
                "",
                "A+B's wedding",
                "Wedd",
                EventType.Wedding,
                LocalDate.parse("2024-07-19"),
                "15:30",
                200,
                1000,
                0,
                mapOf(),
                EventStatus.Upcoming
            )

        )
    ),
    contentEventsToday: UiState<List<Event>> = UiState.Success(
        listOf(
            Event(
                1,
                "",
                "A+B's wedding",
                "Wedd",
                EventType.Wedding,
                LocalDate.parse("2023-07-20"),
                "15:30",
                200,
                1000,
                0,
                mapOf(),
                EventStatus.Upcoming
            ),
            Event(
                1,
                "",
                "A+B's wedding",
                "Wedd",
                EventType.Wedding,
                LocalDate.parse("2023-07-20"),
                "15:30",
                200,
                1000,
                0,
                mapOf(),
                EventStatus.Upcoming
            ),
            Event(
                1,
                "",
                "A+B's wedding",
                "Wedd",
                EventType.Wedding,
                LocalDate.parse("2023-07-20"),
                "15:30",
                200,
                1000,
                0,
                mapOf(),
                EventStatus.Upcoming
            ), Event(
                1,
                "",
                "A+B's wedding",
                "Wedd",
                EventType.Wedding,
                LocalDate.parse("2023-07-20"),
                "15:30",
                200,
                1000, 0,
                mapOf(),
                EventStatus.Upcoming
            )

        )
    ),
    contentEventsPlanning: UiState<List<Event>> = UiState.Success(
        listOf(
            Event(
                1,
                "",
                "A+B's wedding",
                "Wedd",
                EventType.Wedding,
                LocalDate.parse("2023-07-19"),
                "15:30",
                200,
                1000, 0,
                mapOf(),
                EventStatus.Planning
            )
        )
    ),
    contentEventsPast: UiState<List<Event>> = UiState.Success(
        listOf(
            Event(
                1,
                "",
                "A+B's wedding",
                "Wedd",
                EventType.Wedding,
                LocalDate.parse("2023-07-19"),
                "15:30",
                200,
                1000, 0,
                mapOf(),
                EventStatus.Planning
            )
        )
    ),
    onUpcomingCardClick: () -> Unit = {},
    onPlanningCardClick: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TodayEventsCard(contentEventsToday)
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            UpcomingEventsCard(
                contentEventsUpcoming = contentEventsUpcoming,
                onCardClick = onUpcomingCardClick
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
                PlanningEventsCard(
                    contentEventsPlanning = contentEventsPlanning,
                    onCardClick = onPlanningCardClick
                )
                Spacer(modifier = Modifier.width(5.dp))
                PastEventsCard(contentEventsPast = contentEventsPast)
            }
        }

    }
}

