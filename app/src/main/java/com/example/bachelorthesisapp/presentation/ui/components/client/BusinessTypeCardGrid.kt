package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.presentation.ui.components.common.ImageCarrousel
import com.example.bachelorthesisapp.presentation.ui.theme.GreenLight
import com.example.bachelorthesisapp.presentation.ui.theme.RedSoft
import com.example.bachelorthesisapp.presentation.ui.theme.Typography

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BusinessTypeCardGrid(
    event: Event, postsList: List<OfferPost>, onBusinessTypeFilterClick: (String) -> Unit
) {
    var expandedState by remember {
        mutableStateOf(false)
    }
    var postType by remember {
        mutableStateOf("")
    }

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
                            text = post.description,
                            style = Typography.caption,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
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
        verticalItemSpacing = 10.dp,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val vendorsSorted = event.vendors.toList().sortedBy { (_, value) -> value }.toMap()
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
                    .padding(start = 20.dp, end = 20.dp, bottom = 5.dp)
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
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(
                    width = 1.dp, brush = Brush.horizontalGradient(
                        colors = listOf(iconTint, iconTint)
                    )
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${vendorsSorted.keys.toList()[index]}",
                        style = Typography.subtitle1,
                        color = Color.DarkGray
                    )
                }

            }
        }
    }
}