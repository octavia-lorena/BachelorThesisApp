package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.model.BusinessType

@Composable
@Preview
fun EventDetailsBusinessCard(
    business: BusinessEntity = BusinessEntity(
        "",
        "Name",
        "username",
        "email",
        "pss",
        "https://firebasestorage.googleapis.com/v0/b/eventspace-24f7d.appspot.com/o/images%2Fposts%2FSrjjuuIevANOmCBvDPQhFe5MnJo2%2F1691838598076.jpeg?alt=media&token=c1da6e38-35df-4a96-8162-c986ded88224",
        BusinessType.PhotoVideo,
        "Cluj",
        "",
        null,
        null,
        "",
        ""
    ),
    postsList: List<OfferPost> = listOf(),
    onBusinessClick: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(5.dp)
            .clickable {
                // Navigate to business profile
                onBusinessClick(business.id)
            },
        shape = RoundedCornerShape(3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(7.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 0.dp)
                    .background(Color.LightGray, shape = CircleShape)
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
                    placeholder = painterResource(id = R.drawable.profile_picture_placeholder),
                    error = painterResource(id = R.drawable.profile_picture_placeholder)

                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = business.businessName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = business.city)
                Text(text = "${postsList.size} posts")
            }
            Spacer(modifier = Modifier.width(170.dp))
            IconButton(
                //   modifier = Modifier.weight(1f),
                onClick = {
                    // Navigate to business profile
                    onBusinessClick(business.id)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowRight,
                    contentDescription = ""
                )
            }
        }
    }
}