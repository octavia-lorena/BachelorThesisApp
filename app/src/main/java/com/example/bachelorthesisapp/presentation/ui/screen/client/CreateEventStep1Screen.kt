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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.events.CreateEventEvent
import com.example.bachelorthesisapp.data.model.EventType
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessSecondaryAppBar
import com.example.bachelorthesisapp.presentation.ui.components.common.DropdownDateMenu
import com.example.bachelorthesisapp.presentation.ui.components.common.DropdownTimeMenu
import com.example.bachelorthesisapp.presentation.ui.components.common.ErrorText
import com.example.bachelorthesisapp.presentation.ui.components.common.FormTextField
import com.example.bachelorthesisapp.presentation.ui.components.common.LargeDropdownMenu
import com.example.bachelorthesisapp.presentation.ui.components.common.SubmitCreateFormButton
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.ui.theme.WhiteTransparent
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import androidx.compose.material3.Icon as Icon3

@Composable
fun CreateEventsStep1Screen(
    uid: String,
    clientViewModel: ClientViewModel,
    navHostController: NavHostController
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    LaunchedEffect(key1 = context) {
        clientViewModel.validationCreateEventEvents.collect { event ->
            when (event) {
                is ClientViewModel.ValidationEvent.Success -> {
                    navHostController.navigate("create_event_step2/$uid")
                }

                else -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    Scaffold(
        topBar = {
            BusinessSecondaryAppBar(
                title = "New Event",
                navController = navHostController,
                onNavBackClick = {
                    clientViewModel.clearCreateEventState()
                }
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
            CreateEventStep1ScreenContent(clientViewModel, navHostController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventStep1ScreenContent(
    clientViewModel: ClientViewModel,
    navController: NavHostController
) {
    val state = clientViewModel.createEventState
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        clientViewModel.validationCreateEventEvents.collect { event ->
            when (event) {
                is ClientViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Event created successfully!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    navController.popBackStack()
                }

                is ClientViewModel.ValidationEvent.Failure -> {
                    Toast.makeText(
                        context,
                        "Something went wrong!\n Check your internet connection or try again.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Divider(
                modifier = Modifier
                    .width(150.dp)
                    .height(5.dp),
                color = Rose
            )
            Spacer(modifier = Modifier.width(15.dp))
            Divider(
                modifier = Modifier
                    .width(150.dp)
                    .height(5.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 20.dp,
                    bottom = 0.dp
                ),
            userScrollEnabled = true,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TITLE ITEM
            item {
                Column(horizontalAlignment = Alignment.Start) {
                    Spacer(modifier = Modifier.height(2.dp))
                    FormTextField(
                        labelText = stringResource(R.string.Title),
                        value = state.title,
                        onValueChange = {
                            clientViewModel.onCreateEventEvent(
                                CreateEventEvent.TitleChanged(
                                    it
                                )
                            )
                        },
                        error = state.titleError,
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
                                    contentDescription = "Present now",
                                    modifier = Modifier
                                        .tooltipAnchor()
                                        .size(15.dp)
                                )
                            }
                        },
                        keyboardType = KeyboardType.Text
                    )
                    if (state.titleError != null) {
                        ErrorText(text = state.titleError.toString())
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
                            clientViewModel.onCreateEventEvent(
                                CreateEventEvent.DescriptionChanged(
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
            // EVENT TYPE FIELD
            item {
                var selectedIndex by remember { mutableStateOf(-1) }
                val options = enumValues<EventType>()
                LargeDropdownMenu(
                    label = "Event Type",
                    items = options.map { it.name },
                    selectedIndex = selectedIndex,
                    onItemSelected = { index, type ->
                        run {
                            selectedIndex = index
                            clientViewModel.onCreateEventEvent(
                                CreateEventEvent.TypeChanged(type)
                            )
                        }
                    },
                    painterResource = R.drawable.baseline_category_24,
                    initialValue = state.type
                )
                if (state.typeError != null) {
                    ErrorText(text = state.typeError.toString())
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            // DATE FIELD - date picker
            item {
                DropdownDateMenu(
                    label = "Event Date",
                    onItemSelected = { date ->
                        run {
                            clientViewModel.onCreateEventEvent(
                                CreateEventEvent.DateChanged(date)
                            )
                        }
                    },
                    initialDate = state.date
                )
                if (state.dateError != null) {
                    ErrorText(text = state.dateError.toString())
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            // TIME FIELD
            item {
                DropdownTimeMenu(
                    label = "Event Time",
                    onItemSelected = { time ->
                        run {
                            clientViewModel.onCreateEventEvent(
                                CreateEventEvent.TimeChanged(time)
                            )
                        }
                    },
                    initialTime = state.time
                )
                if (state.timeError != null) {
                    ErrorText(text = state.timeError.toString())
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            // NEXT BUTTON
            item {
                SubmitCreateFormButton(
                    onButtonClick = {
                        clientViewModel.onCreateEventEvent(CreateEventEvent.PartialSubmit)
                    },
                    text = stringResource(R.string.Next),
                )

            }
        }
    }
}

