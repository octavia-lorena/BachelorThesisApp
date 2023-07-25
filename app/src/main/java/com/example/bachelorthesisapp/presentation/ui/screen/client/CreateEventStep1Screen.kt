package com.example.bachelorthesisapp.presentation.ui.screen.client

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.bachelorthesisapp.presentation.ui.components.BusinessSecondaryAppBar
import com.example.bachelorthesisapp.presentation.ui.components.DropdownDateMenu
import com.example.bachelorthesisapp.presentation.ui.components.ErrorText
import com.example.bachelorthesisapp.presentation.ui.components.FormTextField
import com.example.bachelorthesisapp.presentation.ui.components.LargeDropdownMenu
import com.example.bachelorthesisapp.presentation.ui.components.SubmitButton
import com.example.bachelorthesisapp.presentation.ui.navigation.Routes
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.WhiteTransparent
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel

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
                    title = "New Event",
                    navController = navHostController
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
                        "Offer posted successfully!",
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
    Box(modifier = Modifier.wrapContentSize()) {
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
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 20.dp,
                bottom = 0.dp
            ),
        userScrollEnabled = true,
        //verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
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
                            tooltip = { Text("Make sure it starts with a capital letter") },
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
                painterResource = R.drawable.baseline_category_24
            )
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
                }
            )
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
                        clientViewModel.onCreateEventEvent(
                            CreateEventEvent.TimeChanged(
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
                            tooltip = { Text("Format must be 24h hh:mm") },
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


        // SUBMIT BUTTON
        item {
            SubmitButton(
                onClick = {
                    clientViewModel.onCreateEventEvent(CreateEventEvent.PartialSubmit)
                },
                text = stringResource(R.string.Next)
            )
        }
    }
}

