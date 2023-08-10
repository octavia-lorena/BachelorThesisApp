package com.example.bachelorthesisapp.presentation.ui.components.business

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bachelorthesisapp.data.model.entities.AppointmentRequest
import com.example.bachelorthesisapp.data.model.entities.ClientEntity
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.IrisBlueDark
import com.example.bachelorthesisapp.presentation.ui.theme.IrisBlueLight
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.ui.theme.dayBackgroundColor
import com.example.bachelorthesisapp.presentation.ui.theme.kalendarBackgroundColor
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarType
import com.himanshoe.kalendar.color.KalendarColor
import com.himanshoe.kalendar.color.KalendarColors
import java.time.LocalDate


@Composable
fun RequestsFrontLayerContent(
    businessId: String,
    clientViewModel: ClientViewModel,
    contentPosts: UiState<List<OfferPost>> = UiState.Loading,
    contentClients: UiState<List<ClientEntity>> = UiState.Loading,
    contentRequests: UiState<List<AppointmentRequest>> = UiState.Loading
) {

    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val eventsState =
        clientViewModel.eventState.collectAsStateWithLifecycle(initialValue = UiState.Loading)

    LaunchedEffect(key1 = pickedDate) {
        Log.d("SELECTED_DATE", "$pickedDate")
        clientViewModel.loadAllEvents()
    }


    when (contentRequests) {
        is UiState.Loading -> {
            Column(verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    backgroundColor = IrisBlueLight,
                    color = IrisBlueLight,
                )
            }
        }

        is UiState.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Kalendar(
                        currentDay = kotlinx.datetime.LocalDate.parse(LocalDate.now().toString()),
                        kalendarType = KalendarType.Firey,
                        kalendarColors = KalendarColors(
                            List(12) { index ->
                                KalendarColor(
                                    backgroundColor = kalendarBackgroundColor[index],
                                    dayBackgroundColor = dayBackgroundColor[index],
                                    headerTextColor = List(12){Color.White}[index]
                                )
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                            .animateContentSize { initialValue, targetValue -> },
                        onDayClick = { date, _ -> pickedDate = LocalDate.parse(date.toString()) }
                    )
                }

                val eventsContent = eventsState.value
                if (contentPosts is UiState.Success && eventsContent is UiState.Success && contentClients is UiState.Success) {
                    val posts = contentPosts.value.filter { it.businessId == businessId }
                    val postsIds = posts.map { it.id }
                    val events =
                        eventsContent.value.filter { it.date == pickedDate }
                    val eventsIds = events.map { it.id }
                    val appointments =
                        contentRequests.value.filter { it.postId in postsIds && it.eventId in eventsIds }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "${appointments.size} appointments on this date",
                            style = Typography.body2,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    items(appointments.size) { index ->
                        val request = appointments[index]
                        val post = posts.first { it.id == request.postId }
                        val event = events.first { it.id == request.eventId }
                        val client = contentClients.value.first { it.id == event.organizerId }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize(
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = LinearOutSlowInEasing
                                    )
                                )
                                .padding(bottom = 3.dp),
                            shape = RoundedCornerShape(2.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = event.name,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Text(
                                        text = event.type.name,
                                        style = Typography.caption,
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    androidx.compose.material.Text(text = "\u2022")
                                    Spacer(modifier = Modifier.width(3.dp))
                                    androidx.compose.material.Text(
                                        text = "organized by ${client.username}",
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        style = Typography.caption,
                                    )

                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Text(
                                        text = "Post: ${post.title}",
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        style = Typography.caption,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        is UiState.Error -> {
        }
    }
}

