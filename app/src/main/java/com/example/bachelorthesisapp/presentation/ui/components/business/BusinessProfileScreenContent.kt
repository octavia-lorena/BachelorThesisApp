package com.example.bachelorthesisapp.presentation.ui.components.business

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.domain.model.BusinessType
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.notifications.PushNotification
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.domain.model.Rating

@Preview
@Composable
fun BusinessProfileScreenContent(
    business: BusinessEntity = BusinessEntity(
        "",
        "Ana - Wedding Photographer",
        "anna_photo",
        "anna@gmail.com",
        "ann",
        "",
        BusinessType.PhotoVideo,
        "Cluj",
        "Str. Florilor, nr. 10",
        1.1,
        1.1,
        "",
        deviceToken = null
    ),
    onPostClick: (Int, PushNotification) -> Unit = { _, _ -> },
    postsState: UiState<List<OfferPost>> = UiState.Success(
        listOf(
            OfferPost(
                1, "", "Full Pack 1", "This is the post description.", listOf(
                    "https://firebasestorage.googleapis.com/v0/b/eventspace-24f7d.appspot.com/o/images%2Fposts%2FSrjjuuIevANOmCBvDPQhFe5MnJo2%2F1691838598076.jpeg?alt=media&token=c1da6e38-35df-4a96-8162-c986ded88224",
                    "https://firebasestorage.googleapis.com/v0/b/eventspace-24f7d.appspot.com/o/images%2Fposts%2FSrjjuuIevANOmCBvDPQhFe5MnJo2%2F1691838602407.jpeg?alt=media&token=66a4faf0-b93c-4c25-a307-671d470aa252",
                    "https://firebasestorage.googleapis.com/v0/b/eventspace-24f7d.appspot.com/o/images%2Fposts%2FSrjjuuIevANOmCBvDPQhFe5MnJo2%2F1691838605854.jpeg?alt=media&token=ead1b5d6-0010-4d73-8316-3822dd34e647"
                ), 2000, Rating(3.3, 1)
            ),
            OfferPost(
                1, "", "Full Pack 1", "This is the post description.", listOf(
                    "https://firebasestorage.googleapis.com/v0/b/eventspace-24f7d.appspot.com/o/images%2Fposts%2FSrjjuuIevANOmCBvDPQhFe5MnJo2%2F1691838598076.jpeg?alt=media&token=c1da6e38-35df-4a96-8162-c986ded88224",
                    "https://firebasestorage.googleapis.com/v0/b/eventspace-24f7d.appspot.com/o/images%2Fposts%2FSrjjuuIevANOmCBvDPQhFe5MnJo2%2F1691838602407.jpeg?alt=media&token=66a4faf0-b93c-4c25-a307-671d470aa252",
                    "https://firebasestorage.googleapis.com/v0/b/eventspace-24f7d.appspot.com/o/images%2Fposts%2FSrjjuuIevANOmCBvDPQhFe5MnJo2%2F1691838605854.jpeg?alt=media&token=ead1b5d6-0010-4d73-8316-3822dd34e647"
                ), 2000, Rating(3.3, 1)
            ),
        )
    ),
    pastEventsState: UiState<List<Event>> = UiState.Loading,
    onRatingClick: (Int, Int) -> Unit = { _, _ -> }
) {

    val context = LocalContext.current

    when (postsState) {
        is UiState.Loading -> {}
        is UiState.Success -> {
            val posts = postsState.value
            LazyColumn(
                modifier = Modifier.padding(bottom = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile header
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .height(170.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .padding(top = 0.dp)
                                        .background(Color.Transparent, shape = CircleShape)
                                        .border(
                                            width = 1.dp,
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color.DarkGray,
                                                    Color.Gray,
                                                    Color.White
                                                )
                                            ),
                                            shape = RoundedCornerShape(50.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape),
                                        model = business.profilePicture,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        placeholder = painterResource(id = R.drawable.profile_picture_placeholder)
                                    )
                                }
                                Spacer(modifier = Modifier.width(50.dp))
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "${posts.size}")
                                    Text(text = "Posts")
                                }
                                Spacer(modifier = Modifier.width(30.dp))

                                if (business.lat != null && business.lng != null) {
                                    Spacer(
                                        modifier = Modifier
                                            .width(50.dp)
                                            .weight(1f)
                                    )
                                    IconButton(onClick = {
                                        val gmmIntentUri =
                                            Uri.parse("google.streetview:cbll=${business.lat},${business.lng}")
                                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                        mapIntent.setPackage("com.google.android.apps.maps")
                                        startActivity(context, mapIntent, null)
                                    }, modifier = Modifier) {
                                        Icon(
                                            imageVector = Icons.Default.Map,
                                            contentDescription = "",
                                            tint = Color.Gray,
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = business.businessName)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = business.city)
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(text = business.address)
                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(
                                thickness = Dp.Hairline, color = Color.Gray,

                            )
                        }
                    }
                }
                items(posts.size) { index ->
                    BusinessProfilePostCard(
                        post = posts[index],
                        onPostClick = onPostClick,
                        business = business,
                        onRatingClick = onRatingClick
                    )
                }
            }
        }

        is UiState.Error -> {}
    }


}