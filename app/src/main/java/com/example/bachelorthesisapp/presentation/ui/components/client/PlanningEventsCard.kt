package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.presentation.ui.theme.Ochre
import com.example.bachelorthesisapp.presentation.ui.theme.OchreDark
import com.example.bachelorthesisapp.presentation.ui.theme.OchreLight
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.core.presentation.UiState

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PlanningEventsCard(
    contentEventsPlanning: UiState<List<Event>> = UiState.Loading,
    onCardClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .height(130.dp)
            .width(180.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 10.dp,
        shape = RoundedCornerShape(30.dp),
        onClick = onCardClick
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .aspectRatio(1f)
                    .padding(top = 0.dp)
                    .background(Color.White, shape = CircleShape)
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Gray,
                                Color.LightGray,
                                Color.White
                            )
                        ),
                        shape = RoundedCornerShape(50.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_pending_actions_24),
                    contentDescription = "",
                    tint = Ochre,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "PLANNING",
                    style = Typography.h1.copy(fontSize = 16.sp),
                    color = MaterialTheme.colors.primaryVariant
                )
                Spacer(modifier = Modifier.width(15.dp))
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .aspectRatio(2f)
                        .padding(top = 0.dp)
                        .background(Ochre, shape = CircleShape)
                        .border(
                            width = 1.dp,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    OchreDark,
                                    OchreLight,
                                    OchreDark
                                )
                            ),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    when (contentEventsPlanning) {
                        is UiState.Success -> {
                            Text(
                                modifier = Modifier.padding(1.dp),
                                text = "${contentEventsPlanning.value.size}",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }

                        else -> {}
                    }

                }
            }
        }
    }
}