package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.EventStatus
import com.example.bachelorthesisapp.data.model.entities.EventType
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Preview
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
    onEventDelete: (Int) -> Unit = {}
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 65.dp)
    ) {
        when (contentEvents) {
            is UiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    backgroundColor = Rose,
                    color = CoralLight
                )
            }

            is UiState.Success -> {
                val eventList = contentEvents.value
                LazyColumn(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxSize(),
                ) {
                    items(eventList.size) { index ->
                        val event = eventList[index]
                        UpcomingEventsExpandableCard(event = event,
                        onEventDelete = onEventDelete)
                    }
                }
            }

            is UiState.Error -> {
                Text(text = contentEvents.cause.toString())
            }
        }
    }
}
