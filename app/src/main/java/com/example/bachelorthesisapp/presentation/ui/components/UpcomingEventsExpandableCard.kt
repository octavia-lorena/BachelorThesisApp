package com.example.bachelorthesisapp.presentation.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.EventStatus
import com.example.bachelorthesisapp.data.model.entities.EventType
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import java.time.LocalDate
import java.time.Period

@ExperimentalMaterialApi
@Composable
fun UpcomingEventsExpandableCard(
    event: Event,
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(5.dp),
        onClick = {
            expandedState = !expandedState
        },
        elevation = 10.dp
    ) {
        // MAIN COLUMN
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // MAIN ROW
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                // TITLE + TYPE COLUMN
                Column(
                    modifier = Modifier
                        .weight(2f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = event.name, overflow = TextOverflow.Ellipsis, maxLines = 1)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = event.type.name)
                }
                // DATE + TIME ELEMENTS COLUMN
                Column(
                    modifier = Modifier
                        .weight(3f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    // TIME LEFT ROW
                    Row(
                        modifier = Modifier.height(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
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
                    Spacer(modifier = Modifier.height(10.dp))
                    // EVENT DATE + TIME ROW
                    Row(
                        modifier = Modifier.height(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                            contentDescription = "",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${event.date}",
                            style = Typography.caption,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_access_time_24),
                            contentDescription = "",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = event.time,
                            style = Typography.caption,
                            color = Color.Gray
                        )

                    }
                }
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(ContentAlpha.medium)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (expandedState) {
                Column(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier.height(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        val differenceValue = event.budget - event.cost
                        var differenceText = ""
                        if (differenceValue < 0)
                            differenceText = " ($differenceValue)"
                        else if (differenceValue > 0)
                            differenceText = " (+$differenceValue)"
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_attach_money_24),
                            contentDescription = "",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Row() {
                            Text(
                                text = "Event budget: ${event.cost}",
                                style = Typography.caption,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            if (differenceValue != 0)
                                Text(
                                    text = differenceText,
                                    style = Typography.caption,
                                    color = if (differenceValue < 0) Color.Red else Color.Green
                                )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        modifier = Modifier.height(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_person_24),
                            contentDescription = "",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${event.guestNumber} guests",
                            style = Typography.caption,
                            color = Color.Black
                        )
                    }
                    Row(
                        modifier = Modifier.height(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_workspaces_24),
                            contentDescription = "",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Vendors",
                            style = Typography.caption,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
@Preview
fun ExpandableCardPreview() {
    UpcomingEventsExpandableCard(
        event = Event(
            1,
            "",
            "A+B's wedding",
            "Wedd",
            EventType.Wedding,
            LocalDate.parse("2023-07-22"),
            "15:30",
            200,
            1000,
            2000,
            mapOf(),
            EventStatus.Upcoming
        )
    )
}