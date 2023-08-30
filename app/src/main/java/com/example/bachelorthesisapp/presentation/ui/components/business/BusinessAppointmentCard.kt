package com.example.bachelorthesisapp.presentation.ui.components.business

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.clients.local.entity.ClientEntity
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.presentation.ui.components.common.SmallSubmitButton
import com.example.bachelorthesisapp.presentation.ui.components.common.SubmitButton
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.GreenDark
import com.example.bachelorthesisapp.presentation.ui.theme.GreenLight
import com.example.bachelorthesisapp.presentation.ui.theme.OffWhite
import com.example.bachelorthesisapp.presentation.ui.theme.RedSoft
import com.example.bachelorthesisapp.presentation.ui.theme.SkyGray
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.ui.theme.WhiteTransparent
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BusinessAppointmentCard(
    post: OfferPost,
    event: Event,
    client: ClientEntity,
    onCancelAppointment: () -> Unit,
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )


    val scope = rememberCoroutineScope()
    var isCancelDialogOpen by remember {
        mutableStateOf(false)
    }
    var cancelButtonEnabled by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        if (event.date.minusDays(30) <= LocalDate.now()) {
            cancelButtonEnabled = false
        }
    }

    if (isCancelDialogOpen) {
        Dialog(
            onDismissRequest = { isCancelDialogOpen = false },
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
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
                    modifier = Modifier
                        .fillMaxSize(1f),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(7.dp),
                        text = "Are you sure you want to cancel this appointment?",
                        color = Color.Gray,
                        style = Typography.caption
                    )
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                isCancelDialogOpen = false
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            border = BorderStroke(
                                width = 1.dp, brush = Brush.horizontalGradient(
                                    listOf(
                                        Color.Gray,
                                        Color.LightGray,
                                        Color.Gray
                                    )
                                )
                            )
                        ) {
                            Text(
                                text = "Cancel",
                                style = Typography.button,
                                color = Color.DarkGray
                            )
                        }
                        Spacer(modifier = Modifier.padding(15.dp))
                        Button(
                            onClick = {
                                isCancelDialogOpen = false
                                scope.launch {
                                    onCancelAppointment()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            border = BorderStroke(
                                width = 1.dp, brush = Brush.horizontalGradient(
                                    listOf(
                                        GreenDark,
                                        GreenLight,
                                        GreenDark
                                    )
                                )
                            )
                        ) {
                            Text(
                                text = "Continue",
                                style = Typography.button,
                                color = Color.DarkGray
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // TOP ROW
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                // EVENT TITLE
                Text(
                    text = event.name,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(5f),
                    style = Typography.body2,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(12.dp))
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
                        contentDescription = "Drop-Down Arrow",
                        tint = Color.DarkGray
                    )
                }
            }
            Spacer(modifier = Modifier.height(3.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = event.type.name,
                    style = Typography.caption,
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(text = "\u2022")
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "organized by ${client.username}",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = Typography.caption,
                )

            }
            Spacer(modifier = Modifier.height(3.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Requests ${post.title}",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = Typography.caption,
                )

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
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                            contentDescription = "",
                            tint = CoralAccent
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${event.date}",
                            style = Typography.caption,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_access_time_24),
                            contentDescription = "",
                            tint = CoralAccent
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = event.time,
                            style = Typography.caption,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.height(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_alternate_email_24),
                            contentDescription = "",
                            tint = CoralAccent
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = client.email,
                            style = Typography.caption,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_phone_24),
                            contentDescription = "",
                            tint = CoralAccent
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = client.phoneNumber!!,
                            style = Typography.caption,
                            color = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
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
                                text = "Cancel Appointment",
                                style = Typography.button,
                                color = Color.DarkGray
                            )
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        PlainTooltipBox(
                            tooltip = {
                                Text(
                                    "Make sure you cancel the appointment only in special and necessary cases!\n" +
                                            "You cannot cancel an appointment for an event taking place in less than 30 days.",
                                    style = Typography.caption,
                                    color = Color.DarkGray
                                )
                            },
                            containerColor = OffWhite
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