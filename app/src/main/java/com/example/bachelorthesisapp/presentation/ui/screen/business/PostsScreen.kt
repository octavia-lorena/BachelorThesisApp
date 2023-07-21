import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.presentation.ui.components.ActionsRow
import com.example.bachelorthesisapp.presentation.ui.components.AddPostFloatingButton
import com.example.bachelorthesisapp.presentation.ui.components.BottomNavigationBarBusiness
import com.example.bachelorthesisapp.presentation.ui.components.BusinessDrawerContent
import com.example.bachelorthesisapp.presentation.ui.components.BusinessHomeAppBar
import com.example.bachelorthesisapp.presentation.ui.components.PostDraggableCard
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.SkyGray
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.BusinessViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.CardSwipeViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState


@Composable
fun BusinessPostsHomeScreen(
    uid: String,
    authViewModel: AuthViewModel,
    businessViewModel: BusinessViewModel,
    navHostController: NavHostController,
    onPostClick: (Int) -> Unit,
    cardsViewModel: CardSwipeViewModel
) {
    val postBusinessState =
        businessViewModel.postBusinessState.collectAsStateWithLifecycle(UiState.Loading)
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        businessViewModel.loadPostData()
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding() + 50.dp, top = 10.dp)
        ) {
            BusinessPostsHomeScreenContent(
                contentPosts = postBusinessState.value,
                onPostClick = onPostClick,
                cardsViewModel = cardsViewModel,
                businessViewModel = businessViewModel,
                navHostController = navHostController
            )
        }
    }
}

@Composable
fun BusinessPostsHomeScreenContent(
    contentPosts: UiState<List<OfferPost>> = UiState.Loading,
    onPostClick: (Int) -> Unit,
    businessViewModel: BusinessViewModel,
    cardsViewModel: CardSwipeViewModel,
    navHostController: NavHostController
) {
    val revealedCardIds by cardsViewModel.revealedCardIdsList.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
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
                val postsList = contentPosts.value
                Log.d("POSTS", postsList.toString())
                LazyColumn(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxSize(),
                ) {
                    items(postsList.size) { index ->
                        val post = postsList[index]
                        PostItem(
                            post = post,
                            businessViewModel = businessViewModel,
                            cardsViewModel = cardsViewModel,
                            revealedCards = revealedCardIds,
                            navHostController = navHostController
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

@Composable
fun PostItem(
    post: OfferPost,
    onPostClick: (postId: Int) -> Unit = {},
    businessViewModel: BusinessViewModel,
    cardsViewModel: CardSwipeViewModel,
    revealedCards: List<Int>,
    navHostController: NavHostController
) {
    val postState = businessViewModel.postCurrentState.collectAsStateWithLifecycle(UiState.Loading)


    var isDialogOpen by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
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
                                try {
                                    businessViewModel.deletePost(post)
                                    Toast.makeText(
                                        context,
                                        "Post successfully deleted.",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } catch (error: RuntimeException) {
                                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT)
                                        .show()
                                }
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

    Box(
        Modifier
            .fillMaxWidth()
            .height(280.dp),
    ) {
        ActionsRow(
            actionIconSize = 56.dp,
            onDelete = {
                //businessViewModel.deletePost(post)
                isDialogOpen = true
                cardsViewModel.reset()
            },
            onEdit = {
                Log.d("EDIT", "requested by ${post.id}")
                when (postState.value) {
                    is UiState.Loading -> {}
                    is UiState.Error -> {}
                    is UiState.Success -> {
                        cardsViewModel.reset()
                        navHostController.navigate("update_post/${post.id}")
                    }
                }

            }
        )
        PostDraggableCard(
            post = post,
            cardHeight = 260.dp,
            isRevealed = revealedCards.contains(post.id),
            cardOffset = -300f,
            onExpand = {
                cardsViewModel.onItemExpanded(post.id)
                businessViewModel.findPostById(post.id)
                businessViewModel.setUpdatePostState(post)
            },
            onCollapse = { cardsViewModel.onItemCollapsed(post.id) })
    }
}
