package com.example.bachelorthesisapp.presentation.ui.components.client

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
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.EventStatus
import com.example.bachelorthesisapp.data.model.entities.EventType
import com.example.bachelorthesisapp.presentation.ui.theme.Green
import com.example.bachelorthesisapp.presentation.ui.theme.GreenDark
import com.example.bachelorthesisapp.presentation.ui.theme.GreenLight
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState
import java.time.LocalDate
import java.time.Period

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun UpcomingEventsCard(
    contentEventsUpcoming: UiState<List<Event>> = UiState.Success(
        listOf(
            Event(1, "", "Event1", "",
            EventType.Wedding, LocalDate.now(), "13:00", 200,
            5000, 4000, emptyMap(), EventStatus.Upcoming
            )
        )
    ),
    onCardClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth(),
//            .animateContentSize(
//                animationSpec = tween(
//                    durationMillis = 300,
//                    easing = LinearOutSlowInEasing
//                )
//            ),
        backgroundColor = Color.White,
        elevation = 10.dp,
        shape = RoundedCornerShape(30.dp),
        onClick = onCardClick
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "UPCOMING",
                    style = Typography.h1.copy(fontSize = 15.sp),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(15.dp))
                Box(
                    modifier = Modifier
                        .size(30.dp)
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
                    when (contentEventsUpcoming) {
                        is UiState.Success -> {
                            Text(
                                modifier = Modifier.padding(1.dp),
                                text = "${contentEventsUpcoming.value.size}",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }

                        else -> {}
                    }

                }
            }
            when (contentEventsUpcoming) {
                is UiState.Success -> {
                    var eventsList =
                        contentEventsUpcoming.value.filter { it.date != LocalDate.now() }
                    if (eventsList.size > 3)
                        eventsList = eventsList.take(3)
                    eventsList.forEachIndexed { index, event ->
                        UpcomingEventElement(event)
                        if (index < 2)
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp),
                                thickness = 1.dp,
                                color = Color.LightGray
                            )
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun UpcomingEventElement(
    event: Event
) {
    Column(
        modifier = Modifier
            .padding(bottom = 2.dp, top = 5.dp)
            .height(70.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.width(180.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(7.dp)
                    .aspectRatio(1f)
                    .padding(top = 0.dp)
                    .background(Rose, shape = AbsoluteCutCornerShape(5.dp)),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = event.name,
                    style = Typography.h3.copy(fontSize = 13.sp),
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = event.type.name, style = Typography.caption.copy(fontSize = 13.sp))
            }

        }
        Spacer(modifier = Modifier.height(1.dp))
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.height(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                val timeToGo =
                    Period.between(LocalDate.now(), event.date)
                val yearsLeft = timeToGo.years
                val monthsLeft = timeToGo.months
                val daysLeft = timeToGo.days

                var yearsText = ""
                if (yearsLeft > 0)
                    yearsText = "${yearsLeft}y, "

                var monthsText = ""
                if (monthsLeft > 0)
                    monthsText = if (daysLeft > 0)
                        "${monthsLeft}m, "
                    else "${monthsLeft}m"


                val daysText = "${daysLeft}d"
                Spacer(modifier = Modifier.width(14.dp))
                Icon(
                    painter = painterResource(id = R.drawable.baseline_hourglass_top_24),
                    contentDescription = "",
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "$yearsText$monthsText$daysText left",
                    style = Typography.caption,
                    color = Color.Gray
                )
            }
//            Spacer(modifier = Modifier.height(3.dp))
//            Row(
//                modifier = Modifier.height(15.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.baseline_calendar_today_24),
//                    contentDescription = "",
//                    tint = Color.Gray
//                )
//                Spacer(modifier = Modifier.width(5.dp))
//                Text(
//                    text = "${event.date}",
//                    style = Typography.caption,
//                    color = Color.Gray
//                )
//            }
        }
    }

}
