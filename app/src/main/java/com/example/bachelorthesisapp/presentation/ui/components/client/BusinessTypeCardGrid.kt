package com.example.bachelorthesisapp.presentation.ui.components.client

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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.presentation.ui.theme.GreenLight
import com.example.bachelorthesisapp.presentation.ui.theme.RedSoft
import com.example.bachelorthesisapp.presentation.ui.theme.Typography

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BusinessTypeCardGrid(
    event: Event,
    postsList: List<OfferPost>,
    onBusinessTypeFilterClick: (String) -> Unit
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    val post =
                        postsList.first { post -> post.id in event.vendors.filter { vendor -> vendor.key.name == postType && vendor.value == post.id }.values }
                    Text(
                        modifier = Modifier.padding(7.dp),
                        text = post.title,
                        color = Color.Gray
                    )
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