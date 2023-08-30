package com.example.bachelorthesisapp.presentation.ui.components.business

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupProperties
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.data.notifications.NotificationData
import com.example.bachelorthesisapp.data.notifications.PushNotification
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.model.BusinessType
import com.example.bachelorthesisapp.data.model.Rating
import com.example.bachelorthesisapp.presentation.ui.components.common.ImageCarrousel
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.gowtham.ratingbar.RatingBar

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun BusinessProfilePostCard(
    post: OfferPost = OfferPost(
        1, "", "Full Pack 1", "This is the post description.", listOf(
            "https://firebasestorage.googleapis.com/v0/b/eventspace-24f7d.appspot.com/o/images%2Fposts%2FSrjjuuIevANOmCBvDPQhFe5MnJo2%2F1691838598076.jpeg?alt=media&token=c1da6e38-35df-4a96-8162-c986ded88224",
            "https://firebasestorage.googleapis.com/v0/b/eventspace-24f7d.appspot.com/o/images%2Fposts%2FSrjjuuIevANOmCBvDPQhFe5MnJo2%2F1691838602407.jpeg?alt=media&token=66a4faf0-b93c-4c25-a307-671d470aa252",
            "https://firebasestorage.googleapis.com/v0/b/eventspace-24f7d.appspot.com/o/images%2Fposts%2FSrjjuuIevANOmCBvDPQhFe5MnJo2%2F1691838605854.jpeg?alt=media&token=ead1b5d6-0010-4d73-8316-3822dd34e647"
        ), 2000, Rating(3.3, 1)
    ),
    onPostClick: (Int, PushNotification) -> Unit = { _, _ -> },
    business: BusinessEntity = BusinessEntity(
        "",
        "BName",
        "username",
        "email",
        "pass",
        null,
        BusinessType.PhotoVideo,
        "city",
        "addr",
        "nr",
        "token"
    ),
    onRatingClick: (Int, Int) -> Unit = { _, _ -> }
) {

    var expanded by remember {
        mutableStateOf(false)
    }
    var rating by remember {
        mutableStateOf(5f)
    }
    var isDialogOpen by remember {
        mutableStateOf(false)
    }

    if (isDialogOpen) {
        Dialog(
            onDismissRequest = { isDialogOpen = false },
            properties = DialogProperties(dismissOnClickOutside = true, dismissOnBackPress = true)
        ) {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(7.dp),
                        text = "Are you sure you want to request this service?",
                        color = Color.Gray,
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
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            ),
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
                                text = "Cancel", style = Typography.button, color = Color.DarkGray
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            onClick = {
                                isDialogOpen = false
                                val notification = PushNotification(
                                    NotificationData(
                                        "Event Space",
                                        "You have a new event collaboration request for ${post.title}"
                                    ), business.deviceToken!!
                                )
                                onPostClick(post.id, notification)

                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            ),
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
                                text = "Request", style = Typography.button, color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(bottom = 10.dp),
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
                Spacer(modifier = Modifier.width(15.dp))
                IconButton(onClick = { expanded = true }, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.MoreVert, contentDescription = ""
                    )
                    DropdownMenu(
                        modifier = Modifier.background(color = Color.White),
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        properties = PopupProperties()
                    ) {
                        DropdownMenuItem(onClick = {
                            isDialogOpen = true
                        }) {
                            Text(
                                text = "Request post for your event",
                                fontSize = 16.sp,
                                color = Color.DarkGray,
                                style = Typography.caption
                            )
                        }
                        DropdownMenuItem(onClick = {}) {
                            Text(
                                text = "Rate",
                                fontSize = 16.sp,
                                color = Color.DarkGray,
                                style = Typography.caption
                            )
                            Spacer(modifier = Modifier.width(width = 8.dp))
                            RatingBar(
                                value = rating,
                                painterEmpty = painterResource(id = R.drawable.baseline_star_border_24),
                                painterFilled = painterResource(id = R.drawable.baseline_star_24),
                                onValueChange = {
                                    rating = it
                                    Log.d("RATING", "$rating")
                                },
                                onRatingChanged = {},
                                size = 20.dp
                            )
                            Spacer(modifier = Modifier.width(width = 10.dp))
                            IconButton(onClick = {
                                isDialogOpen = false
                                val notification = PushNotification(
                                    NotificationData(
                                        "Event Space",
                                        "You have a new event collaboration request for ${post.title}"
                                    ), business.deviceToken!!
                                )
                                onPostClick(post.id, notification)
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_done_outline_24),
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
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
                        text = "${post.price}", color = Color.DarkGray, style = Typography.caption
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


        }
    }
}