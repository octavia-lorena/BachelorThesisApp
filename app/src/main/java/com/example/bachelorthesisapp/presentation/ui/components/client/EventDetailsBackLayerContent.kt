package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.model.BusinessType
import com.example.bachelorthesisapp.data.model.EventStatus
import com.example.bachelorthesisapp.data.model.EventType
import com.example.bachelorthesisapp.data.model.Rating
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.DarkGray
import com.example.bachelorthesisapp.presentation.ui.theme.OffWhite
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period
import kotlin.time.Duration.Companion.seconds

@Composable
fun EventDetailsBackLayerContent(
    event: Event = Event(
        1,
        "",
        "A+B Wedding",
        "A classy wedding, bla bla, family and friends, love and marriage bla bla",
        EventType.Wedding,
        LocalDate.parse("2023-07-25"),
        "13:30",
        100,
        1000,
        200,
        mapOf(
            Pair(BusinessType.Beauty, -1),
            Pair(BusinessType.Catering, 1),
            Pair(BusinessType.Florist, -1),
            Pair(BusinessType.Venue, -1),
            Pair(BusinessType.DecorDesign, 2),
            Pair(BusinessType.Entertainment, -1),
            Pair(BusinessType.Music, -1),
            Pair(BusinessType.PhotoVideo, 3)
        ),
        EventStatus.Planning
    ),
    onBusinessTypeFilterClick: (String) -> Unit = {},
    postsList: List<OfferPost>,
    onEditClick: (Int) -> Unit = {},
    onPublishClick: (Int) -> Unit = {},
    onCollaborationCanceledClicked: (Int) -> Unit = {}
) {
    var enablePublishButton by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = event) {
        enablePublishButton = !event.vendors.values.toList().any { it == -1 }
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        modifier = Modifier
            .padding(start = 0.dp, end = 0.dp, top = 20.dp)
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Finish planning your event",
                            style = Typography.body1,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        IconButton(
                            modifier = Modifier.size(23.dp),
                            onClick = { expanded = true }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                            DropdownMenu(
                                modifier = Modifier.background(color = Color.White),
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                properties = PopupProperties()
                            ) {
                                DropdownMenuItem(onClick = {
                                    onEditClick(event.id)
                                }) {
                                    Icon(
                                        painterResource(id = R.drawable.baseline_edit_24),
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Edit",
                                        fontSize = 16.sp,
                                        color = Color.DarkGray,
                                        style = Typography.caption
                                    )
                                }
                                DropdownMenuItem(
                                    onClick = {
                                        onPublishClick(event.id)
                                    },
                                    enabled = enablePublishButton
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.baseline_done_outline_24),
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Publish",
                                        fontSize = 16.sp,
                                        color = Color.DarkGray,
                                        style = Typography.caption
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val totalVendors = event.vendors.size
                        val unresolved = event.vendors.values.toList().filter { it == -1 }.size
                        Text(
                            text = "$unresolved/$totalVendors",
                            style = Typography.subtitle2,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "steps to go",
                            style = Typography.subtitle2,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = event.type.name,
                        style = Typography.subtitle2,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
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
                            tint = CoralAccent
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${event.date}",
                            style = Typography.subtitle2,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_access_time_24),
                            contentDescription = "",
                            tint = CoralAccent
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = event.time,
                            style = Typography.subtitle2,
                            color = Color.Black
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
                                tint = CoralAccent
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "TODAY",
                                style = Typography.subtitle2,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = ", ${timeLeft.hour}h ${timeLeft.minute}min left",
                                style = Typography.subtitle2,
                                color = Color.Black
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
                                tint = Coral
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "$yearsText$monthsText$daysText left",
                                style = Typography.subtitle2,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.height(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        var color by remember {
                            mutableStateOf(Color.Black)
                        }
                        LaunchedEffect(key1 = event) {
                            if (event.cost > event.budget)
                                color = Color.Red
                        }
                        Text(
                            text = "Budget status",
                            style = Typography.subtitle2,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${event.cost}/${event.budget}",
                            style = Typography.subtitle1,
                            color = color
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = Dp.Hairline,
                        color = OffWhite
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
        // BUSINESS TYPE CARD GRID
        item {
            BusinessTypeCardGrid(
                event = event,
                postsList = postsList,
                onBusinessTypeFilterClick = onBusinessTypeFilterClick
            )
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}