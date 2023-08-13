package com.example.bachelorthesisapp.presentation.ui.screen.business

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.presentation.ui.components.business.BusinessDrawerContent
import com.example.bachelorthesisapp.presentation.ui.components.business.CompletedAppointmentsCard
import com.example.bachelorthesisapp.presentation.ui.components.business.RequestsCard
import com.example.bachelorthesisapp.presentation.ui.components.business.TodayAppointmentsCard
import com.example.bachelorthesisapp.presentation.ui.components.business.UpcomingAppointmentsCard
import com.example.bachelorthesisapp.presentation.ui.components.common.AddPostFloatingButton
import com.example.bachelorthesisapp.presentation.ui.components.common.BottomNavigationBarBusiness
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessHomeAppBar
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.viewmodel.BusinessViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun BusinessHomeScreen(
    uid: String,
    authViewModel: AuthViewModel,
    navHostController: NavHostController,
    askNotificationPermissionCall: () -> Unit,
    clientViewModel: ClientViewModel,
    businessViewModel: BusinessViewModel
) {
    askNotificationPermissionCall()
    authViewModel.subscribeToTopic(uid)
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    val requestsState =
        clientViewModel.requestsState.collectAsStateWithLifecycle(UiState.Loading)
    val appointmentsState =
        clientViewModel.appointmentsState.collectAsStateWithLifecycle(UiState.Loading)
    val eventsState =
        clientViewModel.eventState.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    val postsState =
        clientViewModel.postsState.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    val loadingState by clientViewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = loadingState)
    val scope = rememberCoroutineScope()


    LaunchedEffect(key1 = context) {
        clientViewModel.loadRequests(uid)
        clientViewModel.loadAllPosts()
        clientViewModel.loadAllEvents()
        businessViewModel.clearUpdatePostForm()
        businessViewModel.clearCreatePostForm()
    }

    Scaffold(
        modifier = Modifier.animateContentSize(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearOutSlowInEasing
            )
        ),
        topBar = { BusinessHomeAppBar(title = "My Space", scaffoldState = scaffoldState) },
        bottomBar = {
            BottomNavigationBarBusiness(
                navController = navHostController,
                onItemClick = {
                    var route = it.route
                    val baseRoute = route.split("/")[0]
                    route = "$baseRoute/$uid"
                    navHostController.navigate(route)
                })
        },
        floatingActionButton = {
            AddPostFloatingButton(navHostController = navHostController)
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            BusinessDrawerContent(
                uid = uid,
                authVM = authViewModel,
                navController = navHostController
            )
        },
        drawerGesturesEnabled = true,
        backgroundColor = Color.White
    ) { innerPadding ->
        SwipeRefresh(state = swipeRefreshState, onRefresh = {
            scope.launch {
                clientViewModel.loadRequests(uid)
                clientViewModel.loadAllPosts()
                clientViewModel.loadAllEvents()
            }
        }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding(), top = 10.dp)
            ) {
                BusinessHomeScreenContent(
                    businessId = uid,
                    postContent = postsState.value,
                    eventContent = eventsState.value,
                    appointmentsContent = appointmentsState.value,
                    requestsContent = requestsState.value,
                    onUpcomingCardClick = { navHostController.navigate("appointments/$uid") },
                    onRequestsCardClick = { navHostController.navigate("requests/$uid") },
                    onFeedCardClick = { navHostController.navigate("posts_business/$uid") }
                )
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun BusinessHomeScreenContent(
    businessId: String = "",
    requestsContent: UiState<List<AppointmentRequest>> = UiState.Loading,
    appointmentsContent: UiState<List<AppointmentRequest>> = UiState.Loading,
    postContent: UiState<List<OfferPost>> = UiState.Loading,
    eventContent: UiState<List<Event>> = UiState.Loading,
    onUpcomingCardClick: () -> Unit = {},
    onRequestsCardClick: () -> Unit = {},
    onFeedCardClick: () -> Unit = {}

) {
    LazyColumn(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TodayAppointmentsCard(
                businessId = businessId,
                contentAppointmentsToday = appointmentsContent,
                contentEvents = eventContent,
                contentPosts = postContent
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .height(320.dp)
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 5.dp),
                verticalItemSpacing = 15.dp,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                item {
                    UpcomingAppointmentsCard(
                        businessId = businessId,
                        contentAppointmentsUpcoming = appointmentsContent,
                        contentPosts = postContent,
                        contentEvents = eventContent,
                        onCardClick = onUpcomingCardClick
                    )
                }
                item {
                    RequestsCard(
                        businessId = businessId,
                        contentRequests = requestsContent,
                        contentPosts = postContent,
                        onCardClick = onRequestsCardClick
                    )
                }
                item {
                    CompletedAppointmentsCard(
                        businessId = businessId,
                        contentAppointments = appointmentsContent,
                        contentPosts = postContent,
                        contentEvents = eventContent,
                    )
                }
            }
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.dp)
                    .padding(top = 10.dp, bottom = 10.dp)
                    .clickable { onFeedCardClick() },
                backgroundColor = Color.White,
                elevation = 10.dp,
                shape = RoundedCornerShape(30.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "MY FEED", color = CoralAccent)
                }

            }
        }

    }
}