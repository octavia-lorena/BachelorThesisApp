package com.example.bachelorthesisapp.presentation.ui.screen.client

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material3.Icon as Icon3
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.entities.EventType
import com.example.bachelorthesisapp.data.model.events.CreateEventEvent
import com.example.bachelorthesisapp.data.model.events.UpdateEventEvent
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessSecondaryAppBar
import com.example.bachelorthesisapp.presentation.ui.components.common.DropdownDateMenu
import com.example.bachelorthesisapp.presentation.ui.components.common.ErrorText
import com.example.bachelorthesisapp.presentation.ui.components.common.FormTextField
import com.example.bachelorthesisapp.presentation.ui.components.common.LargeDropdownMenu
import com.example.bachelorthesisapp.presentation.ui.components.common.SubmitButton
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.ui.theme.WhiteTransparent
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel

@Composable
fun UpdateEventScreen(
    eventId: Int, clientViewModel: ClientViewModel, navHostController: NavHostController
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        clientViewModel.validationUpdateEventEvents.collect { event ->
            when (event) {
                is ClientViewModel.ValidationEvent.Success -> {
                    navHostController.popBackStack()
                }

                else -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box {
//        Image(
//            painter = painterResource(id = R.drawable.create_post_background),
//            contentDescription = "background",
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.FillBounds
//        )
        Scaffold(
            topBar = {
                BusinessSecondaryAppBar(
                    title = "Update Event Details", navController = navHostController
                )
            },
            scaffoldState = scaffoldState,
            drawerGesturesEnabled = true,
            backgroundColor = Color.White
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = innerPadding.calculateBottomPadding(),
                        top = 10.dp,
                        start = 20.dp,
                        end = 20.dp
                    )
            ) {
                UpdateEventScreenContent(eventId, clientViewModel, navHostController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateEventScreenContent(
    eventId: Int,
    clientViewModel: ClientViewModel, navController: NavHostController
) {
    val state = clientViewModel.updateEventState
    val context = LocalContext.current

    state.id = eventId

    LaunchedEffect(key1 = context) {
        clientViewModel.validationCreateEventEvents.collect { event ->
            when (event) {
                is ClientViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context, "Event details updated successfully!", Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                }

                is ClientViewModel.ValidationEvent.Failure -> {
                    Toast.makeText(
                        context,
                        "Something went wrong!\n Check your internet connection or try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 20.dp, bottom = 0.dp
            ), userScrollEnabled = true,
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        // NAME ITEM
        item {
            Column(horizontalAlignment = Alignment.Start) {
                Spacer(modifier = Modifier.height(2.dp))
                FormTextField(
                    labelText = stringResource(R.string.Name),
                    value = state.name,
                    onValueChange = {
                        clientViewModel.onUpdateEventEvent(
                            UpdateEventEvent.NameChanged(
                                it
                            )
                        )
                    },
                    error = state.nameError,
                    leadingIcon = null,
                    trailingIcon = {
                        PlainTooltipBox(
                            tooltip = {
                                Text(
                                    "Make sure it starts with a capital letter",
                                    style = Typography.caption
                                )
                            },
                            containerColor = WhiteTransparent
                        ) {
                            Icon3(
                                imageVector = Icons.Filled.QuestionMark,
                                contentDescription = "Question mark",
                                modifier = Modifier
                                    .tooltipAnchor()
                                    .size(15.dp)
                            )
                        }
                    },
                    keyboardType = KeyboardType.Text
                )
                if (state.nameError != null) {
                    ErrorText(text = state.nameError.toString())
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        // DESCRIPTION ITEM
        item {
            Column(horizontalAlignment = Alignment.Start) {
                Spacer(modifier = Modifier.height(2.dp))
                FormTextField(
                    labelText = stringResource(R.string.Description),
                    value = state.description,
                    onValueChange = {
                        clientViewModel.onUpdateEventEvent(
                            UpdateEventEvent.DescriptionChanged(
                                it
                            )
                        )
                    },
                    error = state.descriptionError,
                    leadingIcon = null,
                    trailingIcon = null,
                    keyboardType = KeyboardType.Text
                )
                if (state.descriptionError != null) {
                    ErrorText(text = state.descriptionError.toString())
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        // DATE FIELD - date picker
        item {
            DropdownDateMenu(
                label = "Date",
                initialDate = state.date,
                onItemSelected = { date ->
                    run {
                        clientViewModel.onUpdateEventEvent(
                            UpdateEventEvent.DateChanged(
                                date
                            )
                        )
                    }
                })
            Spacer(modifier = Modifier.height(10.dp))
        }
        // TIME FIELD
        item {
            Column(horizontalAlignment = Alignment.Start) {
                Spacer(modifier = Modifier.height(2.dp))
                FormTextField(
                    labelText = stringResource(R.string.Time),
                    value = state.time,
                    onValueChange = {
                        clientViewModel.onUpdateEventEvent(
                            UpdateEventEvent.TimeChanged(
                                it
                            )
                        )
                    },
                    error = state.timeError,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_access_time_24),
                            contentDescription = "time icon"
                        )
                    },
                    trailingIcon = {
                        PlainTooltipBox(
                            tooltip = {
                                Text(
                                    "Format must be 24h hh:mm",
                                    style = Typography.caption
                                )
                            },
                            containerColor = WhiteTransparent
                        ) {
                            Icon3(
                                imageVector = Icons.Filled.QuestionMark,
                                contentDescription = "Present now",
                                modifier = Modifier
                                    .tooltipAnchor()
                                    .size(15.dp)
                            )
                        }
                    },
                    keyboardType = KeyboardType.Text
                )
                if (state.timeError != null) {
                    ErrorText(text = state.timeError.toString())
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        // BUDGET FIELD
        item {
            Column(horizontalAlignment = Alignment.Start) {
                Spacer(modifier = Modifier.height(2.dp))
                FormTextField(
                    labelText = stringResource(R.string.Budget),
                    value = state.budget,
                    onValueChange = {
                        clientViewModel.onUpdateEventEvent(
                            UpdateEventEvent.BudgetChanged(
                                it
                            )
                        )
                    },
                    error = state.budgetError,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_attach_money_24),
                            contentDescription = "money icon"
                        )
                    },
                    trailingIcon = {
                    },
                    keyboardType = KeyboardType.Number
                )
                if (state.budgetError != null) {
                    ErrorText(text = state.budgetError.toString())
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        // GUEST NUMBER FIELD
        item {
            Column(horizontalAlignment = Alignment.Start) {
                Spacer(modifier = Modifier.height(2.dp))
                FormTextField(
                    labelText = stringResource(R.string.GuestNumber),
                    value = state.guestNumber,
                    onValueChange = {
                        clientViewModel.onUpdateEventEvent(
                            UpdateEventEvent.GuestNumberChanged(
                                it
                            )
                        )
                    },
                    error = state.guestNumberError,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_person_24),
                            contentDescription = "person icon"
                        )
                    },
                    trailingIcon = {
                    },
                    keyboardType = KeyboardType.Text
                )
                if (state.guestNumberError != null) {
                    ErrorText(text = state.guestNumberError.toString())
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // SUBMIT BUTTON
        item {
            SubmitButton(
                onClick = {
                    clientViewModel.onUpdateEventEvent(UpdateEventEvent.Submit)
                }, text = stringResource(R.string.Update)
            )
        }
    }
}

