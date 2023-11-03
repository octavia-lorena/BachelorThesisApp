package com.example.bachelorthesisapp.presentation.ui.screen.business

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.presentation.ui.components.common.BottomNavigationBarBusiness
import com.example.bachelorthesisapp.presentation.ui.components.business.BusinessDrawerContent
import com.example.bachelorthesisapp.presentation.ui.components.business.RequestsScreenContent
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessHomeAppBar
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.presentation.viewmodel.BusinessViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun RequestsScreen(
    uid: String,
    authViewModel: AuthViewModel,
    clientViewModel: ClientViewModel,
    businessViewModel: BusinessViewModel,
    navHostController: NavHostController,
) {

    val requestsState =
        businessViewModel.requestsByBusinessIdState.collectAsStateWithLifecycle()
    val appointmentsState =
        businessViewModel.appointmentsByBusinessIdState.collectAsStateWithLifecycle()
    val businessState =
        clientViewModel.businessResultState.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    val loadingState by clientViewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = loadingState)
    val eventsState =
        clientViewModel.eventState.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    val postsState =
        businessViewModel.postsByBusinessIdState.collectAsStateWithLifecycle()
    val clientsState =
        clientViewModel.clientsState.collectAsStateWithLifecycle(initialValue = UiState.Loading)

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        businessViewModel.getRequestsByBusinessId(uid)
        businessViewModel.getPostsByBusinessId(uid)
       // businessViewModel.getAppointmentsByBusinessId(uid)
        clientViewModel.findBusinessById(uid)
        clientViewModel.loadAllEvents()
        clientViewModel.loadAllClients()
    }

    Scaffold(
        topBar = {
            BusinessHomeAppBar(
                title = "Requests",
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
        backgroundColor = MaterialTheme.colors.background
    ) { innerPadding ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                scope.launch {
                    businessViewModel.getRequestsByBusinessId(uid)
                    clientViewModel.findBusinessById(uid)
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding(), top = 10.dp)
            ) {
                RequestsScreenContent(
                    businessId = uid,
                    contentRequests = requestsState.value,
                    contentBusiness = businessState.value,
                    clientViewModel = clientViewModel,
                    contentEvents = eventsState.value,
                    contentPosts = postsState.value,
                    contentClients = clientsState.value,
                    contentAppointments = appointmentsState.value
                )
            }
        }
    }
}