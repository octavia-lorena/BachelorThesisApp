import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NextPlan
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.presentation.ui.components.common.BottomNavigationBarClient
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessHomeAppBar
import com.example.bachelorthesisapp.presentation.ui.components.business.ClientDrawerContent
import com.example.bachelorthesisapp.presentation.ui.components.client.PlanningEventsScreenContent
import com.example.bachelorthesisapp.presentation.ui.components.common.TabItem
import com.example.bachelorthesisapp.presentation.ui.components.client.UpcomingEventsScreenContent
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.SkyGray
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.presentation.ui.components.common.CreateEventExpandableFloatingButton
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventsScreen(
    uid: String,
    authViewModel: AuthViewModel,
    clientViewModel: ClientViewModel,
    navHostController: NavHostController,
    onEventDelete: (Int) -> Unit
) {
    val eventPlanningState =
        clientViewModel.eventPlanningState.collectAsStateWithLifecycle(UiState.Loading)
    val eventUpcomingState =
        clientViewModel.eventUpcomingState.collectAsStateWithLifecycle(UiState.Loading)
    val deletedEventState =
        clientViewModel.deletedEventState.collectAsStateWithLifecycle(UiState.Loading)

    val scaffoldState = rememberScaffoldState()
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    var isDialogOpen by remember {
        mutableStateOf(false)
    }
    var eventId by remember {
        mutableStateOf(0)
    }
    val context = LocalContext.current
    val loadingState by clientViewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = loadingState)
    val listState = rememberLazyListState()
    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    LaunchedEffect(key1 = deletedEventState.value) {
        when (val deletedEvent = deletedEventState.value) {
            is UiState.Loading -> {}
            is UiState.Success -> {
                Toast.makeText(context, "Event deleted successfully.", Toast.LENGTH_SHORT).show()
            }

            is UiState.Error -> {
                Toast.makeText(context, deletedEvent.cause.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    if (isDialogOpen) {
        Dialog(
            onDismissRequest = { isDialogOpen = false },
            properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)
        ) {
            Card(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(120.dp)
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(7.dp),
                        text = "Are you sure you want to delete this post?",
                        color = Color.Gray
                    )
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { isDialogOpen = false },
                            modifier = Modifier
                                .height(50.dp)
                                .width(100.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = SkyGray)
                        ) {
                            Text(text = "Cancel", color = Color.White, style = Typography.button)
                        }
                        Spacer(modifier = Modifier.padding(15.dp))
                        Button(
                            onClick = {
                                isDialogOpen = false
                                onEventDelete(eventId)
                            },
                            modifier = Modifier
                                .height(50.dp)
                                .width(100.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Coral)
                        ) {
                            Text(text = "Delete", style = Typography.button)
                        }
                    }
                }
            }
        }
    }


    LaunchedEffect(Unit) {
        clientViewModel.loadAllEventsByOrganizerId()
        clientViewModel.clearCreateEventState()
    }

    val tabs = listOf(
        TabItem(
            title = "Upcoming",
            icon = Icons.Filled.Upcoming,
            screen = {
                UpcomingEventsScreenContent(
                    contentEvents = eventUpcomingState.value,
                    onEventDelete = { id ->
                        onEventDelete(id)
                    },
                    listState = listState
                )
            }
        ),
        TabItem(
            title = "Planning",
            icon = Icons.Filled.NextPlan,
            screen = {
                PlanningEventsScreenContent(
                    contentEvents = eventPlanningState.value,
                    onEventClick = { eventId ->
                        scope.launch {
                            clientViewModel.findEventById(eventId)
                            navHostController.navigate("event_details/$eventId")
                        }
                    },
                    listState = listState
                )
            }
        )
    )

    Scaffold(
        topBar = {
            BusinessHomeAppBar(
                title = "My Events",
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
            CreateEventExpandableFloatingButton(navHostController = navHostController, uid = uid, expanded = expandedFab)
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
                    .padding(bottom = innerPadding.calculateBottomPadding() + 50.dp, top = 0.dp)
            ) {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                    backgroundColor = Color.White,
                    contentColor = CoralAccent
                ) {
                    tabs.forEachIndexed { index, item ->
                        Tab(
                            selected = index == pagerState.currentPage,
                            text = { Text(text = item.title, color = Color.DarkGray) },
                            icon = { Icon(item.icon, "", tint = CoralAccent) },
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        )
                    }
                }
                HorizontalPager(
                    pageCount = tabs.size,
                    state = pagerState,
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    tabs[pagerState.currentPage].screen()
                }

            }
        }
    }
}

