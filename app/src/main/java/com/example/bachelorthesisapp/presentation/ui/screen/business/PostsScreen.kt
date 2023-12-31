package com.example.bachelorthesisapp.presentation.ui.screen.business

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.presentation.ui.components.common.BottomNavigationBarBusiness
import com.example.bachelorthesisapp.presentation.ui.components.business.BusinessDrawerContent
import com.example.bachelorthesisapp.presentation.ui.components.business.PostItem
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessHomeAppBar
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.BusinessViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.CardSwipeViewModel
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.presentation.ui.components.common.AddPostExpandableFloatingButton
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch


@Composable
fun BusinessPostsHomeScreen(
    uid: String,
    authViewModel: AuthViewModel,
    businessViewModel: BusinessViewModel,
    navHostController: NavHostController,
    cardsViewModel: CardSwipeViewModel
) {
    val posts = businessViewModel.postsByBusinessIdState.collectAsStateWithLifecycle()
    val scaffoldState = rememberScaffoldState()
    val listState = rememberLazyListState()
    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }
    val loadingState by businessViewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = loadingState)
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        businessViewModel.getPostsByBusinessId(uid)
    }

    Scaffold(
        topBar = {
            BusinessHomeAppBar(
                title = "My Posts",
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
        floatingActionButton = {
            AddPostExpandableFloatingButton(
                navHostController = navHostController,
                expanded = expandedFab
            )
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
                    businessViewModel.getPostsByBusinessId(uid)
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding() + 10.dp, top = 0.dp)
            ) {
                BusinessPostsHomeScreenContent(
                    contentPosts = posts.value,
                    businessViewModel = businessViewModel,
                    cardsViewModel = cardsViewModel,
                    listState = listState
                ) { post ->
                    businessViewModel.setUpdatePostState(post)
                    navHostController.navigate("update_post/${post.id}")
                }
            }
        }
    }
}

@Composable
fun BusinessPostsHomeScreenContent(
    contentPosts: UiState<List<OfferPost>> = UiState.Loading,
    businessViewModel: BusinessViewModel,
    cardsViewModel: CardSwipeViewModel,
    listState: LazyListState,
    onEditClicked: (OfferPost) -> Unit,
) {
    val revealedCardIds by cardsViewModel.revealedCardIdsList.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        businessViewModel.clearUpdatePostForm()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp)
    ) {
        when (contentPosts) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    backgroundColor = Rose,
                    color = CoralLight
                )
            }

            is UiState.Success -> {
                val postsList = contentPosts.value.reversed()
                Log.d("POSTS", postsList.toString())
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxSize(),
                ) {
                    items(postsList.size,
                        key = { index ->
                            postsList[index].id
                        }) { index ->
                        val post = postsList[index]
                        PostItem(
                            post = post,
                            businessViewModel = businessViewModel,
                            cardsViewModel = cardsViewModel,
                            revealedCards = revealedCardIds,
                            onEditClicked = onEditClicked
                        )
                    }
                }
            }

            is UiState.Error -> {
                Text(text = contentPosts.cause.toString())
            }
        }
    }
}