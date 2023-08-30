package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.model.EventStatus
import com.example.bachelorthesisapp.data.model.EventType
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UpcomingEventsScreenContent(
    contentEvents: UiState<List<Event>> = UiState.Success(
        listOf(
            Event(
                1,
                "",
                "A+B's wedding",
                "Wedd",
                EventType.Wedding,
                LocalDate.parse("2023-07-22"),
                "15:30",
                200,
                1000,
                0,
                mapOf(),
                EventStatus.Upcoming
            ),
            Event(
                1,
                "",
                "A+B's wedding",
                "Wedd",
                EventType.Wedding,
                LocalDate.parse("2023-07-22"),
                "15:30",
                200,
                1000,
                0,
                mapOf(),
                EventStatus.Upcoming
            )
        )
    ),
    onEventDelete: (Int) -> Unit = {},
    listState: LazyListState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 65.dp)
    ) {
        when (contentEvents) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(10.dp),
                    backgroundColor = CoralLight,
                    color = CoralAccent
                )
            }

            is UiState.Success -> {
                val eventList = contentEvents.value
                LazyColumn(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxSize(),
                    state = listState,
                    horizontalAlignment = Alignment.Start
                ) {
                    items(eventList.size) { index ->
                        val event = eventList[index]
                        if (index == 0) {
                            androidx.compose.material3.Text(
                                text = event.date.format(DateTimeFormatter.ofPattern("d MMM uuuu")),
                                style = Typography.body2,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        if (index > 0) {
                            val previousEvent = eventList[index - 1]
                            if (previousEvent.date != event.date)
                                androidx.compose.material3.Text(
                                    text = event.date.format(DateTimeFormatter.ofPattern("d MMM uuuu")),
                                    style = Typography.body2,
                                    color = Color.Gray
                                )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        UpcomingEventsExpandableCard(
                            event = event,
                            onEventDelete = onEventDelete
                        )
                    }
                }
            }

            is UiState.Error -> {
                Text(text = contentEvents.cause.toString())
            }
        }
    }
}
