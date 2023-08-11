package com.example.bachelorthesisapp.presentation.ui.components.business

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.appointment_requests.local.entity.AppointmentRequest
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.Green
import com.example.bachelorthesisapp.presentation.ui.theme.GreenDark
import com.example.bachelorthesisapp.presentation.ui.theme.GreenLight
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.core.presentation.UiState
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import kotlin.time.Duration.Companion.seconds

@Composable
fun TodayAppointmentsCard(
    businessId: String,
    contentAppointmentsToday: UiState<List<AppointmentRequest>>,
    contentEvents: UiState<List<Event>>,
    contentPosts: UiState<List<OfferPost>>
) {

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(top = 2.dp),
//            .animateContentSize(
//                animationSpec = tween(
//                    durationMillis = 300,
//                    easing = LinearOutSlowInEasing
//                )
 //           ),
        backgroundColor = Color.White,
        elevation = 10.dp,
        shape = RoundedCornerShape(30.dp)
    ) {
        when (contentAppointmentsToday) {
            is UiState.Success -> {
                if (contentPosts is UiState.Success && contentEvents is UiState.Success) {
                    val posts = contentPosts.value.filter { it.businessId == businessId }
                    val postsIds = posts.map { it.id }
                    val events = contentEvents.value.filter { it.date == LocalDate.now() }
                    val eventsIds = events.map { it.id }
                    val appointments =
                        contentAppointmentsToday.value.filter { it.postId in postsIds && it.eventId in eventsIds }
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "TODAY", style = Typography.h1, color = Color.Black)
                            Spacer(modifier = Modifier.width(20.dp))
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .aspectRatio(2f)
                                    .padding(top = 0.dp)
                                    .background(Green, shape = CircleShape)
                                    .border(
                                        width = 1.dp,
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(
                                                GreenDark,
                                                GreenLight,
                                                GreenDark
                                            )
                                        ),
                                        shape = RoundedCornerShape(50.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.padding(1.dp),
                                    text = "${contentAppointmentsToday.value.size}",
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    fontSize = 15.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        appointments.forEach { appointment ->
                            val post = posts.first { it.id == appointment.postId }
                            val event = events.filter { it.id == appointment.eventId }
                                .firstOrNull { it.date == LocalDate.now() }
                            if (event != null)
                                TodayAppointmentElement(
                                    post = post,
                                    event = event
                                )

                        }
                    }
                }
            }
            else -> {}
        }

    }
}

@Composable
fun TodayAppointmentElement(
    event: Event,
    post: OfferPost
) {
    Row(
        modifier = Modifier
            .padding(bottom = 5.dp)
            .height(50.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.width(180.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .aspectRatio(1f)
                    .padding(top = 0.dp)
                    .background(Coral, shape = AbsoluteCutCornerShape(5.dp)),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = event.name,
                    style = Typography.h3,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = event.type.name, style = Typography.caption)
            }

        }
        Spacer(modifier = Modifier.width(10.dp))
        Divider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight(0.75F),
            thickness = 1.dp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.width(15.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.height(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                var currentTime by remember { mutableStateOf(LocalTime.now()) }
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(1.seconds)
                        currentTime = LocalTime.now()
                    }
                }
                val timeLeft = LocalTime.parse(event.time)
                    .minus(Duration.ofHours(currentTime.hour.toLong()))
                    .minus(Duration.ofMinutes(currentTime.minute.toLong()))
                Icon(
                    painter = painterResource(id = R.drawable.baseline_access_time_24),
                    contentDescription = "",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "${timeLeft.hour}h ${timeLeft.minute}min left",
                    style = Typography.caption,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(3.dp))

        }

    }
}