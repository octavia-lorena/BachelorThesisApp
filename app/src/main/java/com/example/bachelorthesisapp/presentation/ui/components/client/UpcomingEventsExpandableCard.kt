package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.model.EventStatus
import com.example.bachelorthesisapp.data.model.EventType
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.RedSoft
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.ui.theme.WhiteTransparent
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterialApi
@Composable
fun UpcomingEventsExpandableCard(
    event: Event, onEventDelete: (Int) -> Unit
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )
    var cancelButtonEnabled by remember {
        mutableStateOf(true)
    }
    var isCancelDialogOpen by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (event.date.minusDays(30) <= LocalDate.now()) {
            cancelButtonEnabled = false
        }
    }

    if (isCancelDialogOpen) {
        Dialog(
            onDismissRequest = { isCancelDialogOpen = false }, properties = DialogProperties(
                dismissOnClickOutside = true, dismissOnBackPress = true
            )
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
                    modifier = Modifier.fillMaxSize(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(7.dp),
                        text = "Are you sure you want to cancel this appointment?",
                        style = Typography.caption
                    )
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { isCancelDialogOpen = false },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            border = BorderStroke(
                                width = 1.dp, brush = Brush.horizontalGradient(
                                    listOf(
                                        Color.Gray, Color.LightGray, Color.Gray
                                    )
                                )
                            )
                        ) {
                            Text(
                                text = "Cancel", style = Typography.button, color = Color.DarkGray
                            )
                        }
                        Spacer(modifier = Modifier.padding(15.dp))
                        Button(
                            onClick = {
                                expandedState = false
                                isCancelDialogOpen = false
                                scope.launch {
                                    onEventDelete(event.id)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            border = BorderStroke(
                                width = 1.dp, brush = Brush.horizontalGradient(
                                    listOf(
                                        CoralAccent, Coral, CoralAccent
                                    )
                                )
                            )
                        ) {
                            Text(
                                text = "Continue", style = Typography.button, color = Color.DarkGray
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
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300, easing = LinearOutSlowInEasing
                )
            )
            .padding(bottom = 10.dp)
            .clickable {
                expandedState = !expandedState
            },
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        // MAIN COLUMN
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // MAIN ROW
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                // TITLE + TYPE COLUMN
                Column(
                    modifier = Modifier.weight(2f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = event.name,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = Typography.h6,
                        color = Color.DarkGray,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = event.type.name,
                        style = Typography.caption,
                    )
                }
                // DATE + TIME ELEMENTS COLUMN
                Column(
                    modifier = Modifier.weight(3f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    // TIME LEFT ROW
                    Row(
                        modifier = Modifier.height(15.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {

                        val timeToGo = Period.between(LocalDate.now(), event.date)
                        val yearsLeft = timeToGo.years
                        val monthsLeft = timeToGo.months
                        val daysLeft = timeToGo.days

                        var yearsText = ""
                        if (yearsLeft > 0) yearsText = "${yearsLeft}y, "

                        var monthsText = ""
                        if (monthsLeft > 0) monthsText = if (daysLeft > 0) "${monthsLeft}m, "
                        else "${monthsLeft}m"

                        val daysText = "${daysLeft}d"

                        Spacer(modifier = Modifier.width(14.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_hourglass_top_24),
                            contentDescription = "",
                            tint = CoralAccent
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "$yearsText$monthsText$daysText left",
                            style = Typography.caption,
                            color = Color.Black
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
                            tint = CoralAccent
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${event.date}", style = Typography.caption, color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_access_time_24),
                            contentDescription = "",
                            tint = CoralAccent
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = event.time, style = Typography.caption, color = Color.Black
                        )

                    }
                }
                IconButton(modifier = Modifier
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
            AnimatedVisibility(expandedState) {
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
                        if (differenceValue < 0) differenceText = " ($differenceValue)"
                        else if (differenceValue > 0) differenceText = " (+$differenceValue)"
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_attach_money_24),
                            contentDescription = "",
                            tint = CoralAccent
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Row {
                            Text(
                                text = "Event budget: ${event.cost}",
                                style = Typography.caption,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            if (differenceValue != 0) Text(
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
                            tint = CoralAccent
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${event.guestNumber} guests",
                            style = Typography.caption,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { isCancelDialogOpen = true },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.White,
                                disabledBackgroundColor = Color.Gray
                            ),
                            border = BorderStroke(
                                width = 1.dp, brush = Brush.horizontalGradient(
                                    listOf(
                                        CoralAccent,
                                        Coral,
                                        CoralAccent
                                    )
                                )
                            ),
                            enabled = cancelButtonEnabled
                        ) {
                            Text(
                                text = "Cancel Event",
                                style = Typography.button,
                                color = Color.DarkGray
                            )
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        PlainTooltipBox(
                            tooltip = {
                                Text(
                                    "Make sure you cancel the appointment only in special and necessary cases!\n" + "You cannot cancel an appointment for an event taking place in less than 30 days.",
                                    style = Typography.caption,
                                    color = Color.DarkGray
                                )
                            }, containerColor = WhiteTransparent
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = "",
                                modifier = Modifier
                                    .tooltipAnchor()
                                    .size(15.dp),
                                tint = RedSoft
                            )
                        }
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
    UpcomingEventsExpandableCard(event = Event(
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
    ), onEventDelete = {})
}