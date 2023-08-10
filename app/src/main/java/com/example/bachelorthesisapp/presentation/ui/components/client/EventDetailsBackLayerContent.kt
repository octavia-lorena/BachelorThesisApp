package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.presentation.ui.theme.DarkGray
import com.example.bachelorthesisapp.presentation.ui.theme.IrisBlue
import com.example.bachelorthesisapp.presentation.ui.theme.IrisBlueDark
import com.example.bachelorthesisapp.presentation.ui.theme.IrisBlueLight
import com.example.bachelorthesisapp.presentation.ui.theme.SkyGray
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period
import kotlin.time.Duration.Companion.seconds

@Composable
fun EventDetailsBackLayerContent(
    event: Event,
    onBusinessTypeFilterClick: (String) -> Unit = {},
    postsList: List<OfferPost> = listOf(),
    onEditClick: (Int) -> Unit = {},
    onPublishClick: (Int) -> Unit = {}
) {
    var enablePublishButton by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = event) {
        enablePublishButton = !event.vendors.values.toList().any { it == -1 }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        IrisBlueDark,
                        IrisBlue,
                        IrisBlueLight,
                        Color.White,
                        Color.White,
                        //Color.White,
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(start = 0.dp, end = 0.dp, top = 0.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()

                ) {
                    Column(
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Finish planning your event",
                            style = Typography.body1,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = event.type.name,
                            style = Typography.subtitle2,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        val totalVendors = event.vendors.size
                        val unresolved = event.vendors.values.toList().filter { it == -1 }.size
                        Text(
                            text = "$unresolved/$totalVendors steps to go",
                            style = Typography.subtitle2,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = event.description,
                            style = Typography.subtitle2,
                            color = DarkGray
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.height(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                                contentDescription = "",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "${event.date}",
                                style = Typography.subtitle2,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_access_time_24),
                                contentDescription = "",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = event.time,
                                style = Typography.subtitle2,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.height(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            if (event.date == LocalDate.now()) {
                                // Calculate the time left
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
                                    painter = painterResource(id = R.drawable.baseline_hourglass_top_24),
                                    contentDescription = "",
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "TODAY",
                                    style = Typography.subtitle2,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = ", ${timeLeft.hour}h ${timeLeft.minute}min left",
                                    style = Typography.subtitle2,
                                    color = Color.White
                                )
                            } else {
                                // Calculate the days left
                                val timeToGo = Period.between(LocalDate.now(), event.date)
                                val yearsLeft = timeToGo.years
                                val monthsLeft = timeToGo.months
                                val daysLeft = timeToGo.days

                                var yearsText = ""
                                if (yearsLeft > 0) yearsText = "${yearsLeft}y, "

                                var monthsText = ""
                                if (monthsLeft > 0) monthsText =
                                    if (daysLeft > 0) "${monthsLeft}m, "
                                    else "${monthsLeft}m"

                                val daysText = "${daysLeft}d"
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_hourglass_top_24),
                                    contentDescription = "",
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "$yearsText$monthsText$daysText left",
                                    style = Typography.subtitle2,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }

            item {

                BusinessTypeCardGrid(
                    event = event,
                    postsList = postsList,
                    onBusinessTypeFilterClick = onBusinessTypeFilterClick
                )
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { onEditClick(event.id) },
                        modifier = Modifier
                            .height(40.dp)
                            .width(140.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = SkyGray)
                    ) {
                        Text(text = "Edit", style = Typography.button, color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        onClick = { onPublishClick(event.id) },
                        modifier = Modifier
                            .height(40.dp)
                            .width(140.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = SkyGray),
                        enabled = enablePublishButton
                    ) {
                        Text(text = "Publish", style = Typography.button, color = Color.White)
                    }
                }
            }
        }
    }
}