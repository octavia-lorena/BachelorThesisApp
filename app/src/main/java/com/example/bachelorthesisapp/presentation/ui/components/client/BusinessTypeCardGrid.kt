package com.example.bachelorthesisapp.presentation.ui.components.client

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupProperties
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.notifications.NotificationData
import com.example.bachelorthesisapp.data.notifications.PushNotification
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.presentation.ui.components.business.ImageCarrousel
import com.example.bachelorthesisapp.presentation.ui.components.common.SmallSubmitButton
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.GreenLight
import com.example.bachelorthesisapp.presentation.ui.theme.OffWhite
import com.example.bachelorthesisapp.presentation.ui.theme.RedSoft
import com.example.bachelorthesisapp.presentation.ui.theme.SkyGray
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.gowtham.ratingbar.RatingBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BusinessTypeCardGrid(
    event: Event,
    postsList: List<OfferPost>,
    onBusinessTypeFilterClick: (String) -> Unit,
    onCollaborationCanceledClicked: (Int) -> Unit
) {
    var expandedState by remember {
        mutableStateOf(false)
    }
    var postType by remember {
        mutableStateOf("")
    }
    var isDeleteExpanded by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    if (expandedState) {
        Dialog(
            onDismissRequest = { expandedState = false },
            properties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true)
        ) {
            val post =
                postsList.first { post -> post.id in event.vendors.filter { vendor -> vendor.key.name == postType && vendor.value == post.id }.values }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .padding(10.dp),
                shape = RoundedCornerShape(5.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            modifier = Modifier.weight(6f),
                            text = post.title,
                            style = Typography.h6,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    // Photo carrousel
                    ImageCarrousel(
                        images = post.images
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.padding(start = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_star_24),
                                contentDescription = null,
                                tint = Color.DarkGray
                            )
                            Text(
                                text = "${post.rating.value}",
                                color = Color.DarkGray,
                                style = Typography.caption
                            )

                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_attach_money_24),
                                contentDescription = null,
                                tint = Color.DarkGray
                            )
                            Text(
                                text = "${post.price}",
                                color = Color.DarkGray,
                                style = Typography.caption
                            )

                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = post.description, style = Typography.caption
                        )
                    }
                    FilledIconButton(
                        onClick = { isDeleteExpanded = true },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = Coral)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_delete_forever_24),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }

    if (isDeleteExpanded) {
        Dialog(
            onDismissRequest = { isDeleteExpanded = false },
            properties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true)
        ) {
            androidx.compose.material.Card(
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
                    androidx.compose.material.Text(
                        modifier = Modifier.padding(7.dp),
                        text = "Are you sure you want to cancel this collaboration?",
                        style = Typography.caption
                    )
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { isDeleteExpanded = false },
                            modifier = Modifier
                                .height(50.dp)
                                .width(100.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = SkyGray)
                        ) {
                            androidx.compose.material.Text(
                                text = "Cancel",
                                color = Color.White,
                                style = Typography.button
                            )
                        }
                        Spacer(modifier = Modifier.padding(15.dp))
                        Button(
                            onClick = {
                                isDeleteExpanded = false
                                try {
                                    onCollaborationCanceledClicked(event.id)
                                    Toast.makeText(
                                        context,
                                        "Collaboration was canceled.",
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
                            androidx.compose.material.Text(
                                text = "Delete",
                                style = Typography.button
                            )
                        }
                    }
                }
            }
        }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier
            .heightIn(50.dp, 220.dp)
            .fillMaxWidth()
            .padding(top = 2.dp, bottom = 0.dp),
        verticalItemSpacing = 15.dp,
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        val vendorsSorted =
            event.vendors.toList().sortedBy { (_, value) -> value }.toMap()
        items(vendorsSorted.keys.size) { index ->
            var id by remember {
                mutableStateOf(R.drawable.outline_cancel_24)
            }
            var done by remember {
                mutableStateOf(false)
            }
            var iconTint by remember {
                mutableStateOf(Color.Black)
            }
            LaunchedEffect(key1 = event) {
                if (vendorsSorted.values.toList()[index] != -1) {
                    id = R.drawable.baseline_task_alt_24
                    done = true
                    iconTint = GreenLight
                } else {
                    id = R.drawable.outline_cancel_24
                    done = false
                    iconTint = RedSoft
                }
            }
            Card(
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
                    .clickable {
                        if (done) {
                            // show a pop up with post details
                            expandedState = true
                            val type = vendorsSorted.keys.toList()[index].name
                            postType = type
                        } else {
                            // filter the vendors list
                            val type = vendorsSorted.keys.toList()[index].name
                            onBusinessTypeFilterClick(type)
                        }
                    },
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
                colors = CardDefaults.cardColors(containerColor = iconTint)
            ) {
                Row(
                    modifier = Modifier.height(15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "${vendorsSorted.keys.toList()[index]}",
                        modifier = Modifier.weight(2f),
                        style = Typography.subtitle1,
                        color = Color.White
                    )
                    Icon(
                        painterResource(id = id),
                        contentDescription = "",
                        modifier = Modifier.weight(1f),
                        tint = Color.White
                    )
                }

            }
        }
    }
}