package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
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
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import java.time.LocalDate
import java.time.Period

@ExperimentalMaterialApi
@Composable
fun PlanningEventsClickableCard(
    event: Event,
    onEventClick: (Int) -> Unit,
    navHostController: NavHostController
) {
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
            // Navigate to the Event Details Screen
            onEventClick(event.id)
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
                            painter = painterResource(id = R.drawable.baseline_pending_actions_24),
                            contentDescription = "",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        val totalVendors = event.vendors.size
                        val resolved = event.vendors.filter { it.value != -1 }.size
                        Text(
                            text = "$resolved/$totalVendors vendors",
                            style = Typography.caption,
                            color = Color.Gray
                        )

                    }
                }
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(ContentAlpha.medium),
                    onClick = {
                        onEventClick(event.id)
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowCircleRight,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

//@ExperimentalMaterialApi
//@Composable
//@Preview
//fun PlanningExpandableCardPreview() {
//    PlanningEventsClickableCard(
//        event = Event(
//            1,
//            "",
//            "A+B's wedding",
//            "Wedd",
//            EventType.Wedding,
//            LocalDate.parse("2023-07-22"),
//            "15:30",
//            200,
//            1000,
//            2000,
//            mapOf(),
//            EventStatus.Upcoming
//        )
//    )
//}