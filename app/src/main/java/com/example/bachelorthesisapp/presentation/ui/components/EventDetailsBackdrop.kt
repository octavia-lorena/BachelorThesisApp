package com.example.bachelorthesisapp.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.BusinessType
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.Period
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun EventDetailsBackdrop(
    event: Event,
    onBusinessTypeFilterClick: (String) -> Unit = {},
    businessState: UiState<List<BusinessEntity>>
) {
    val pagerState = rememberPagerState(initialPage = 1)
    val coroutineScope = rememberCoroutineScope()

    var businessType by remember {
        mutableStateOf("")
    }

    BackdropScaffold(modifier = Modifier,
        scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
        frontLayerShape = RoundedCornerShape(30.dp),
        frontLayerScrimColor = Color.Unspecified,
        frontLayerBackgroundColor = Color.DarkGray,
        backLayerBackgroundColor = Color.White,
        peekHeight = 60.dp,
        stickyFrontLayer = false,
        frontLayerElevation = 20.dp,
        headerHeight = 200.dp,
        // Back layer displays the event details info and the business type selection mechanism
        backLayerContent = {
            LazyColumn(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    Text(text = "Finish planning your event")
                }
                item {
                    Text(
                        text = event.type.name, style = Typography.caption, color = Color.DarkGray
                    )
                }
                item {
                    val totalVendors = event.vendors.size
                    val resolved = event.vendors.map { it.value != -1 }.size
                    Text(
                        text = "$resolved/$totalVendors steps to go",
                        style = Typography.caption,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    Text(text = event.description)
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
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
                            text = "${event.date}", style = Typography.caption, color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_access_time_24),
                            contentDescription = "",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = event.time, style = Typography.caption, color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Row(
                        modifier = Modifier.height(15.dp),
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
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "TODAY", style = Typography.caption, color = Color.Gray
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = ", ${timeLeft.hour}h ${timeLeft.minute}min left",
                                style = Typography.caption,
                                color = Color.Gray
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
                            if (monthsLeft > 0) monthsText = if (daysLeft > 0) "${monthsLeft}m, "
                            else "${monthsLeft}m"

                            val daysText = "${daysLeft}d"
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
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier
                            .heightIn(50.dp, 210.dp)
                            .fillMaxWidth()
                            .padding(top = 2.dp, bottom = 0.dp),
                        verticalItemSpacing = 15.dp,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        items(event.vendors.keys.size) { index ->
                            var id by remember {
                                mutableStateOf(R.drawable.outline_cancel_24)
                            }
                            var done by remember {
                                mutableStateOf(false)
                            }
                            LaunchedEffect(key1 = event) {
                                if (event.vendors.values.toList()[index] != -1) {
                                    id = R.drawable.baseline_task_alt_24
                                    done = true
                                } else {
                                    id = R.drawable.outline_cancel_24
                                    done = false
                                }
                            }
                            Card(modifier = Modifier
                                .size(50.dp)
                                .padding(5.dp), onClick = {
                                if (done) {
                                    // show a pop up with post details
                                } else {
                                    // filter the vendors list
                                    val type = event.vendors.keys.toList()[index].name
                                    businessType = type
                                    onBusinessTypeFilterClick(type)
                                }
                            }) {
                                Row(
                                    modifier = Modifier.height(15.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "${event.vendors.keys.toList()[index]}",
                                        modifier = Modifier.weight(2f)
                                    )
                                    Icon(
                                        painterResource(id = id),
                                        contentDescription = "",
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                            }
                        }
                    }
                }
            }
        },
        appBar = {},
        // Front layer contains the list of the business filtered by the business type selected in the back layer
        frontLayerContent = {
            HorizontalPager(state = pagerState, pageCount = 1) {
                when (businessState) {
                    is UiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(10.dp),
                            backgroundColor = Rose,
                            color = CoralLight
                        )
                    }

                    is UiState.Success -> {
                        // Display the list of businesses filtered by type
                        EventDetailsFrontLayerContent(businessesList = businessState.value)
                    }

                    is UiState.Error -> {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = "Could not load the page.", color = Color.White)
                    }
                }
            }
        })
}