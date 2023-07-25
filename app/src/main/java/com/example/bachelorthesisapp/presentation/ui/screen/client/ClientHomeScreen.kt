package com.example.bachelorthesisapp.presentation.ui.screen.client

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.BadgedBox
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Badge
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.EventStatus
import com.example.bachelorthesisapp.data.model.entities.EventType
import com.example.bachelorthesisapp.presentation.ui.components.BottomNavigationBarClient
import com.example.bachelorthesisapp.presentation.ui.components.BusinessHomeAppBar
import com.example.bachelorthesisapp.presentation.ui.components.ClientDrawerContent
import com.example.bachelorthesisapp.presentation.ui.components.CreateEventFloatingButton
import com.example.bachelorthesisapp.presentation.ui.components.PastEventsCard
import com.example.bachelorthesisapp.presentation.ui.components.PlanningEventsCard
import com.example.bachelorthesisapp.presentation.ui.components.TodayEventsCard
import com.example.bachelorthesisapp.presentation.ui.components.UpcomingEventsCard
import com.example.bachelorthesisapp.presentation.ui.theme.OffWhite
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import kotlin.time.Duration.Companion.days

@Composable
fun ClientHomeScreen(
    uid: String,
    authViewModel: AuthViewModel,
    clientViewModel: ClientViewModel,
    navHostController: NavHostController,
) {
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

    LaunchedEffect(key1 = context) {
        clientViewModel.loadEventData()
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
                uid = uid,
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
                clientViewModel.loadEventData()
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
                    contentEventsPast = eventPastState.value
                )
            }
        }
    }


}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
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
    contentEventsPast: UiState<List<Event>> = UiState.Loading
) {
    LazyColumn(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp)
            .fillMaxSize(),
    ) {
        item {
            TodayEventsCard(contentEventsToday)
            Spacer(modifier = Modifier.height(15.dp))
        }
        item {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .height(290.dp)
                    .fillMaxWidth()
                    .padding(top = 2.dp, bottom = 0.dp),
                verticalItemSpacing = 15.dp,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                item {
                    UpcomingEventsCard(contentEventsUpcoming = contentEventsUpcoming)
                }
                item {
                    PlanningEventsCard(contentEventsPlanning = contentEventsPlanning)
                }
                item {
                    PastEventsCard(contentEventsPast = contentEventsPast)
                }
            }
        }

    }
}

