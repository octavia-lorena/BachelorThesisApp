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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.viewmodel.BusinessViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.CardSwipeViewModel
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent

@Composable
fun PostItem(
    post: OfferPost,
    businessViewModel: BusinessViewModel,
    cardsViewModel: CardSwipeViewModel,
    revealedCards: List<Int>,
    onEditClicked: (OfferPost) -> Unit = {}
) {
    val postState = businessViewModel.postState1
    var isDialogOpen by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
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
                    modifier = Modifier
                        .fillMaxSize(1f),
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
                                        Color.Gray,
                                        Color.LightGray,
                                        Color.Gray
                                    )
                                )
                            )
                        ) {
                            Text(
                                text = "Cancel",
                                style = Typography.button,
                                color = Color.DarkGray
                            )
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
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            border = BorderStroke(
                                width = 1.dp, brush = Brush.horizontalGradient(
                                    listOf(
                                        CoralAccent,
                                        Coral,
                                        CoralAccent
                                    )
                                )
                            )
                        ) {
                            Text(
                                text = "Delete",
                                style = Typography.button,
                                color = Color.DarkGray
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
        ActionsRow(
            actionIconSize = 56.dp,
            onDelete = {
                isDialogOpen = true
                cardsViewModel.reset()
            },
            onEdit = {
                Log.d("EDIT", "requested by $post")
                when (postState.value) {
                    is Resource.Loading -> {}
                    is Resource.Error -> {}
                    is Resource.Success -> {
                        cardsViewModel.reset()
                        onEditClicked(post)
                    }
                }

            }
        )
        PostDraggableCard(
            post = post,
            isRevealed = revealedCards.contains(post.id),
            cardOffset = -300f,
            onExpand = {
                cardsViewModel.onItemExpanded(post.id)
                businessViewModel.findPostById(post.id)
                businessViewModel.setUpdatePostState(post)
            }
        ) { cardsViewModel.onItemCollapsed(post.id) }
    }
}