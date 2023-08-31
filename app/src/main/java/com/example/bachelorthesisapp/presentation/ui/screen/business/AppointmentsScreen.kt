package com.example.bachelorthesisapp.presentation.ui.screen.business

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.data.clients.local.entity.ClientEntity
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.model.RequestStatus
import com.example.bachelorthesisapp.presentation.ui.components.business.BusinessAppointmentCard
import com.example.bachelorthesisapp.presentation.ui.components.common.BottomNavigationBarBusiness
import com.example.bachelorthesisapp.presentation.ui.components.business.BusinessDrawerContent
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessHomeAppBar
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.BusinessViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.data.model.EventStatus
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentsScreen(
    uid: String,
    authViewModel: AuthViewModel,
    clientViewModel: ClientViewModel,
    businessViewModel: BusinessViewModel,
    navHostController: NavHostController,
) {

    val appointmentsState =
        clientViewModel.appointmentsState.collectAsStateWithLifecycle(UiState.Loading)
    val businessState =
        clientViewModel.businessResultState.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    val loadingState by clientViewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = loadingState)
    val eventsState =
        clientViewModel.eventState.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    val postsState =
        clientViewModel.postBusinessState.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    val clientsState =
        clientViewModel.clientsState.collectAsStateWithLifecycle(initialValue = UiState.Loading)

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        clientViewModel.loadRequests(uid)
        clientViewModel.findBusinessById(uid)
        clientViewModel.loadAllEvents()
        clientViewModel.findPostsByBusinessId(uid)
        clientViewModel.loadAllClients()
        //  clientViewModel.loadAppointments(uid)
    }

    Scaffold(
        topBar = {
            BusinessHomeAppBar(
                title = "Appointments",
                scaffoldState = scaffoldState
            )
        },
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
        scaffoldState = scaffoldState,
        drawerContent = {
            BusinessDrawerContent(
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
                    clientViewModel.loadRequests(uid)
                    clientViewModel.findBusinessById(uid)
                    clientViewModel.loadAllEvents()
                    clientViewModel.findPostsByBusinessId(uid)
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding() + 50.dp, top = 10.dp)
            ) {
                AppointmentsScreenContent(
                    uid = uid,
                    contentAppointments = appointmentsState.value,
                    contentBusiness = businessState.value,
                    businessViewModel = businessViewModel,
                    contentEvents = eventsState.value,
                    contentPosts = postsState.value,
                    contentClients = clientsState.value
                )
            }
        }
    }
}

@Composable
fun AppointmentsScreenContent(
    uid: String,
    contentAppointments: UiState<List<AppointmentRequest>> = UiState.Success(
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
    businessViewModel: BusinessViewModel,
    contentEvents: UiState<List<Event>> = UiState.Loading,
    contentPosts: UiState<List<OfferPost>> = UiState.Loading,
    contentClients: UiState<List<ClientEntity>> = UiState.Loading,

    ) {
    val context = LocalContext.current

    when (contentAppointments) {
        is UiState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    backgroundColor = CoralLight,
                    color = CoralAccent,
                )
            }

        }

        is UiState.Success -> {
            Log.d("REQUEST", "SUCCESS")
            LazyColumn(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                // The current business' appointments
                var appointmentsList = contentAppointments.value
                if (contentPosts is UiState.Success && contentEvents is UiState.Success && contentClients is UiState.Success) {
                    // all events with status != Past, sorted by date
                    val eventsList = contentEvents.value.filter { it.status != EventStatus.Past }
                        .sortedBy { it.date }
                    val eventIds = eventsList.map { it.id }
                    appointmentsList = appointmentsList.filter { it.eventId in eventIds }
                    // business' posts
                    val posts = contentPosts.value.filter { it.businessId == uid }
                    items(appointmentsList.size) { index ->
                        val appointment =
                            appointmentsList[index]
                        Log.d("APPOINTMEN", appointment.toString())
                        val post = posts.first { it.id == appointment.postId}
                        val event = eventsList.first { it.id == appointment.eventId }
                        val client =
                            contentClients.value.firstOrNull { it.id == event.organizerId }
                        if (client != null) {
                            Log.d("NEW_REQUEST_CARD", "rid ${appointment.id}, cid ${client.id}")
                        }
                        if (contentBusiness is UiState.Success && client != null) {
                            val business = contentBusiness.value
                            if (index == 0) {
                                Text(
                                    text = event.date.format(DateTimeFormatter.ofPattern("d MMM uuuu")),
                                    style = Typography.body2,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                            }

                            if (index > 0) {
                                val previousAppointment = appointmentsList[index - 1]
                                val previousEvent =
                                    eventsList.first { it.id == previousAppointment.eventId }
                                if (previousEvent.date != event.date)
                                    Text(
                                        text = event.date.format(DateTimeFormatter.ofPattern("d MMM uuuu")),
                                        style = Typography.body2,
                                        color = Color.Gray
                                    )
                                Spacer(modifier = Modifier.height(20.dp))
                            }
                            BusinessAppointmentCard(
                                post = post,
                                event = event,
                                client = client,
                            ) {
                                businessViewModel.cancelAppointment(
                                    appointment.id,
                                    business,
                                    event,
                                    post,
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