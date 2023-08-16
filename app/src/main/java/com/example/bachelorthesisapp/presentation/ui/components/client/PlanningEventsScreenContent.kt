package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlanningEventsScreenContent(
    contentEvents: UiState<List<Event>> = UiState.Loading,
    onEventClick: (Int) -> Unit,
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
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(10.dp),
                    backgroundColor = CoralLight,
                    color = CoralAccent
                )
            }

            is UiState.Success -> {
                val eventList = contentEvents.value
                LazyColumn(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxSize(),
                    state = listState
                ) {
                    items(eventList.size) { index ->
                        val event = eventList[index]
                        PlanningEventsClickableCard(
                            event = event,
                            onEventClick = onEventClick
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
