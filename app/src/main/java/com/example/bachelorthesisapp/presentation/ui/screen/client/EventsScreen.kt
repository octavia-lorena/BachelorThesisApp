import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.presentation.ui.components.BottomNavigationBarClient
import com.example.bachelorthesisapp.presentation.ui.components.BusinessHomeAppBar
import com.example.bachelorthesisapp.presentation.ui.components.ClientDrawerContent
import com.example.bachelorthesisapp.presentation.ui.components.CreateEventFloatingButton
import com.example.bachelorthesisapp.presentation.ui.components.PlanningEventsScreenContent
import com.example.bachelorthesisapp.presentation.ui.components.TabItem
import com.example.bachelorthesisapp.presentation.ui.components.UpcomingEventsScreenContent
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.CardSwipeViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventsScreen(
    uid: String,
    authViewModel: AuthViewModel,
    clientViewModel: ClientViewModel,
    navHostController: NavHostController,
    onPostClick: (Int) -> Unit,
    cardsViewModel: CardSwipeViewModel
) {
    val eventState =
        clientViewModel.eventState.collectAsStateWithLifecycle(UiState.Loading)
    val eventPlanningState =
        clientViewModel.eventPlanningState.collectAsStateWithLifecycle(UiState.Loading)
    val eventUpcomingState =
        clientViewModel.eventUpcomingState.collectAsStateWithLifecycle(UiState.Loading)

    val scaffoldState = rememberScaffoldState()
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(key1 = true) {
        clientViewModel.loadEventData()
    }

    val tabs = listOf(
        TabItem(
            title = "Upcoming",
            icon = Icons.Filled.Upcoming,
            screen = {
                UpcomingEventsScreenContent(
                    contentEvents = eventUpcomingState.value,
                )
            }
        ),
        TabItem(
            title = "Planning",
            icon = Icons.Filled.NextPlan,
            screen = {
                PlanningEventsScreenContent(
                    contentEvents = eventPlanningState.value,
                    navHostController = navHostController
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding() + 50.dp, top = 0.dp)
        ) {
            // CustomTabs()
            //Spacer(modifier = Modifier.height(50.dp))
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = Rose
            ) {
                tabs.forEachIndexed { index, item ->
                    Tab(
                        selected = index == pagerState.currentPage,
                        text = { Text(text = item.title) },
                        icon = { Icon(item.icon, "", tint = Color.White) },
                        onClick = {
                            coroutineScope.launch {
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

