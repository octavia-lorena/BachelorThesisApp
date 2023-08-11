package com.example.bachelorthesisapp.presentation.ui.components.business

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupProperties
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.domain.model.BusinessType
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.domain.model.Rating
import com.example.bachelorthesisapp.data.notifications.NotificationData
import com.example.bachelorthesisapp.data.notifications.PushNotification
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.gowtham.ratingbar.RatingBar

@Preview
@Composable
fun BusinessProfilePostCard(
    post: OfferPost = OfferPost(
        1,
        "",
        "Full Pack 1",
        "This is the post description.",
        listOf(
        ),
        2000,
        Rating(3.3, 1)
    ),
    onPostClick: (Int, PushNotification) -> Unit = { _, _ -> },
    business: BusinessEntity = BusinessEntity(
        "", "BName", "username", "email", "pass", null, BusinessType.PhotoVideo,
        "city", "addr", null, null, "nr", "token"
    ),
    onRatingClick: (Int, Int) -> Unit = { _, _ -> }
) {

    val context = LocalContext.current
    val bitmap = remember {
        mutableStateOf<Bitmap?>(null)
    }
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
            properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)
        ) {
            Card(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(120.dp)
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(7.dp),
                        text = "Are you sure you want to request this service?",
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
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text(
                                text = "Cancel",
                                color = Color.White,
                                style = Typography.button
                            )
                        }
                        Spacer(modifier = Modifier.padding(15.dp))
                        Button(
                            onClick = {
                                isDialogOpen = false
                                //  Log.d("DEVICE_TOKEN_BUSINESS", business.deviceToken!!)
                                val notification = PushNotification(
                                    NotificationData(
                                        "Event Space",
                                        "You have a new event collaboration request for ${post.title}"
                                    ),
                                    business.deviceToken!!
                                )
                                onPostClick(post.id, notification)
                            },
                            modifier = Modifier
                                .height(50.dp)
                                .width(100.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Coral)
                        ) {
                            Text(
                                text = "Request",
                                style = Typography.button
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
            .heightIn(350.dp, 400.dp)
            .padding(top = 5.dp),
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(text = post.title)
                Spacer(modifier = Modifier.width(15.dp))
                Button(
                    onClick = {
                        isDialogOpen = true
                    },
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(30.dp)
                    // .background(color = Color.Gray)
                    ,
                    shape = RoundedCornerShape(2.dp)
                ) {
                    Text(text = "Request", fontSize = 10.sp)
                }
                IconButton(onClick = { expanded = true }, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = ""
                    )
                    DropdownMenu(
                        expanded = expanded, onDismissRequest = { expanded = false },
                        properties = PopupProperties()
                    ) {
                        DropdownMenuItem(onClick = { /*TODO*/ }) {
                            Text(
                                text = "Rate",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(width = 8.dp))
                            RatingBar(
                                value = rating,
                                painterEmpty = painterResource(id = R.drawable.baseline_star_border_24),
                                painterFilled = painterResource(id = R.drawable.baseline_star_24),
                                onValueChange = {
                                    rating = it
                                    Log.d("RATING", "$rating")
                                    onRatingClick(post.id, rating.toInt())
                                },
                                onRatingChanged = {},
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            // Photo carrousel
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp),
                state = rememberLazyListState(initialFirstVisibleItemIndex = 0)
            ) {
                items(post.images.size) { index ->
                    val imgUri = post.images[index]
//                    imgUri.let {
//                        val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
//                        context.contentResolver.takePersistableUriPermission(imgUri, flag)
//                        val source = ImageDecoder
//                            .createSource(context.contentResolver, it)
//                        bitmap.value = ImageDecoder.decodeBitmap(source)
//
//                        bitmap.value?.let { btm ->
//                            Image(
//                                bitmap = btm.asImageBitmap(),
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .size(200.dp)
//                                    .padding(5.dp),
//                                contentScale = ContentScale.Crop
//                            )
//                        }
//                    }
//                    AsyncImage(
//                        model = ImageRequest.Builder(LocalContext.current)
//                            .data(post.images[index])
//                            .build(),
//                        contentDescription = null,
//                        contentScale = ContentScale.Inside,
//                        modifier = Modifier.size(200.dp)
//                    )
//                    Image(
//                        modifier = Modifier.fillMaxSize(),
//                        painter = rememberAsyncImagePainter(model = post.images[index]),
//                        contentDescription = "",
//                        contentScale = ContentScale.FillBounds
//                    )
                }
            }
            Row(
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
                    Text(text = "${post.rating.value}", color = Color.DarkGray)

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
                        color = Color.DarkGray
                    )

                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = post.description)
         }
    }
}