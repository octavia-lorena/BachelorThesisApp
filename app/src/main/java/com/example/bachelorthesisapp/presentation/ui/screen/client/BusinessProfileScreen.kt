package com.example.bachelorthesisapp.presentation.ui.screen.client

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
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
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessProfileAppBar
import com.example.bachelorthesisapp.presentation.ui.components.business.BusinessProfileScreenContent
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.presentation.ui.components.common.LoadingScreen
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch

@Composable
fun BusinessProfileScreen(
    businessId: String,
    eventId: Int,
    navHostController: NavHostController,
    clientViewModel: ClientViewModel
) {

    val businessState =
        clientViewModel.businessResultState.collectAsStateWithLifecycle(UiState.Loading).value
    val postsState =
        clientViewModel.postBusinessState.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    val pastEventsState =
        clientViewModel.eventPastBusinessState.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    val context = LocalContext.current
    val loadingState by clientViewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = loadingState)
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = context) {
        clientViewModel.findBusinessById(businessId)
        clientViewModel.findPostsByBusinessId(businessId)
    }

    when (businessState) {
        is UiState.Loading -> {
            LoadingScreen(navHostController = navHostController)
        }

        is UiState.Success -> {
            val business = businessState.value
            Scaffold(
                topBar = {
                    BusinessProfileAppBar(
                        title = business.username,
                        navController = navHostController
                    )
                }, drawerGesturesEnabled = true, backgroundColor = Color.White
            ) { innerPadding ->
                SwipeRefresh(
                    state = swipeRefreshState,
                    onRefresh = {
                        scope.launch {
                            clientViewModel.findBusinessById(businessId)
                            clientViewModel.findPostsByBusinessId(businessId)
                        }
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = innerPadding.calculateBottomPadding(), top = 10.dp)
                    ) {

                        BusinessProfileScreenContent(
                            business = business,
                            onPostClick = { postId, notification ->
                                clientViewModel.createRequest(
                                    eventId = eventId,
                                    postId = postId,
                                    notification
                                )
                            },
                            postsState = postsState.value,
                            onRatingClick = { postId, ratingValue ->
                                clientViewModel.ratePost(
                                    postId,
                                    ratingValue
                                )
                            },
                            pastEventsState = pastEventsState.value
                        )
                    }
                }
            }

        }

        is UiState.Error -> {}
    }


}