package com.example.bachelorthesisapp.presentation.ui.components.business

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.viewmodel.BusinessViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.CardSwipeViewModel
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PostItem(
    post: OfferPost,
    businessViewModel: BusinessViewModel,
    cardsViewModel: CardSwipeViewModel,
    revealedCards: List<Int>,
    onEditClicked: (OfferPost) -> Unit = {}
) {
    val postState = businessViewModel.postByIdState.collectAsStateWithLifecycle()
    val deletedPostState = businessViewModel.postResponseState.collectAsStateWithLifecycle()

    var isDialogOpen by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(deletedPostState.value) {
        when (deletedPostState.value) {
            is UiState.Success -> {
                Toast.makeText(context, "Post deleted successfully.", Toast.LENGTH_SHORT)
                    .show()
                businessViewModel.resetPostResponseState()
            }

            is UiState.Error -> {
                Toast.makeText(context, "Error deleting post.", Toast.LENGTH_SHORT)
                    .show()
            }

            is UiState.Loading -> {
                Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    if (isDialogOpen) {
        Dialog(
            onDismissRequest = { isDialogOpen = false },
            properties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true)
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
                    modifier = Modifier.fillMaxSize(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(7.dp),
                        text = "Are you sure you want to delete this post?",
                        style = Typography.caption
                    )
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                isDialogOpen = false
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            border = BorderStroke(
                                width = 1.dp, brush = Brush.horizontalGradient(
                                    listOf(
                                        Color.Gray, Color.LightGray, Color.Gray
                                    )
                                )
                            )
                        ) {
                            Text(
                                text = "Cancel", style = Typography.button, color = Color.DarkGray
                            )
                        }
                        Spacer(modifier = Modifier.padding(15.dp))
                        Button(
                            onClick = {
                                isDialogOpen = false
                                scope.launch {
                                    businessViewModel.deletePost(post)
                                }
//                                try {
//
//                                    Toast.makeText(
//                                        context, "Post successfully deleted.", Toast.LENGTH_SHORT
//                                    ).show()
//                                } catch (error: RuntimeException) {
//                                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT)
//                                        .show()
//                                }
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            border = BorderStroke(
                                width = 1.dp, brush = Brush.horizontalGradient(
                                    listOf(
                                        CoralAccent, Coral, CoralAccent
                                    )
                                )
                            )
                        ) {
                            Text(
                                text = "Delete", style = Typography.button, color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .height(330.dp),
    ) {
        ActionsRow(actionIconSize = 56.dp, onDelete = {
            isDialogOpen = true
            cardsViewModel.reset()
        }, onEdit = {
            businessViewModel.getPostById(post.id)
            Log.d("EDIT", "requested by ${post.id}")
            when (val postValue = postState.value) {
                is UiState.Error -> {
                    Log.d("EDIT", "Error ${post.id}")

                }

                is UiState.Success -> {
                    val posst = postValue.value
                    Log.d("EDIT", "Success ${post.id}")
                    cardsViewModel.reset()
                    onEditClicked(post)
                }

                else -> {
                }
            }

        })
        PostDraggableCard(post = post,
            isRevealed = revealedCards.contains(post.id),
            cardOffset = -300f,
            onExpand = {
                Log.d("EXPAND", post.id.toString())
                cardsViewModel.onItemExpanded(post.id)
                businessViewModel.getPostById(post.id)
                businessViewModel.setUpdatePostState(post)
            },
            onCollapse = { cardsViewModel.onItemCollapsed(post.id) })
    }
}