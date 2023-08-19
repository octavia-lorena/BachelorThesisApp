package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.RoseDark
import com.example.bachelorthesisapp.presentation.ui.theme.RoseLight
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.core.presentation.UiState

@Composable
fun PastEventsCard(contentEventsPast: UiState<List<Event>> = UiState.Loading) {
    Card(
        modifier = Modifier
            .height(130.dp)
            .width(180.dp),
        backgroundColor = Color.White,
        elevation = 10.dp,
        shape = RoundedCornerShape(30.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .aspectRatio(1f)
                    .padding(top = 0.dp)
                    .background(Color.White, shape = CircleShape)
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Gray,
                                Color.LightGray,
                                Color.White
                            )
                        ),
                        shape = RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_done_outline_24),
                    contentDescription = "",
                    tint = Rose,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(0.dp))
                Text(
                    text = "PAST EVENTS",
                    style = Typography.h1.copy(fontSize = 16.sp),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .aspectRatio(2f)
                        .padding(top = 0.dp)
                        .background(Rose, shape = CircleShape)
                        .border(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    RoseDark,
                                    RoseLight,
                                    RoseDark
                                )
                            ),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    when (contentEventsPast) {
                        is UiState.Success -> {
                            Text(
                                modifier = Modifier.padding(1.dp),
                                text = "${contentEventsPast.value.size}",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }

                        else -> {}
                    }

                }
            }
        }
    }
}