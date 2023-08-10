package com.example.bachelorthesisapp.presentation.ui.components.business

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.entities.AppointmentRequest
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.EventStatus
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.model.entities.RequestStatus
import com.example.bachelorthesisapp.presentation.ui.theme.Green
import com.example.bachelorthesisapp.presentation.ui.theme.GreenDark
import com.example.bachelorthesisapp.presentation.ui.theme.GreenLight
import com.example.bachelorthesisapp.presentation.ui.theme.Ochre
import com.example.bachelorthesisapp.presentation.ui.theme.OchreDark
import com.example.bachelorthesisapp.presentation.ui.theme.OchreLight
import com.example.bachelorthesisapp.presentation.ui.theme.OffWhite
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.RoseDark
import com.example.bachelorthesisapp.presentation.ui.theme.RoseLight
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState
import java.time.LocalDate

@Composable
fun CompletedAppointmentsCard(
    businessId: String = "",
    contentAppointments: UiState<List<AppointmentRequest>> = UiState.Success(
        listOf()
    ),
    contentEvents: UiState<List<Event>> = UiState.Loading,
    contentPosts: UiState<List<OfferPost>> = UiState.Loading,
) {
    Card(
        modifier = Modifier
            .height(130.dp)
            .fillMaxWidth(),
//            .animateContentSize(
//                animationSpec = tween(
//                    durationMillis = 300,
//                    easing = LinearOutSlowInEasing
//                )
//            ),
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
                    tint = Green,
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
                    text = "COMPLETED",
                    style = Typography.h1.copy(fontSize = 16.sp),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(35.dp)
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
                    when (contentAppointments) {
                        is UiState.Success -> {
                            if (contentEvents is UiState.Success && contentPosts is UiState.Success) {
                                val posts =
                                    contentPosts.value.filter { it.businessId == businessId }
                                val postsIds = posts.map { it.id }
                                val events =
                                    contentEvents.value.filter { it.status == EventStatus.Past }
                                val eventsIds = events.map { it.id }
                                val appointmentsList =
                                    contentAppointments.value.filter { it.postId in postsIds && it.eventId in eventsIds }
                                Text(
                                    modifier = Modifier.padding(1.dp),
                                    text = "${appointmentsList.size}",
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        else -> {}
                    }

                }
            }
        }
    }
}