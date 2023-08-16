package com.example.bachelorthesisapp.presentation.ui.screen.client

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.domain.model.BusinessType
import com.example.bachelorthesisapp.data.model.events.CreateEventEvent
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessSecondaryAppBar
import com.example.bachelorthesisapp.presentation.ui.components.common.DropdownWithCheckboxesMenu
import com.example.bachelorthesisapp.presentation.ui.components.common.ErrorText
import com.example.bachelorthesisapp.presentation.ui.components.common.FormTextField
import com.example.bachelorthesisapp.presentation.ui.components.common.SubmitButton
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.data.model.events.CreatePostEvent
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import kotlinx.coroutines.delay

@Composable
fun CreateEventsStep2Screen(
    uid: String,
    clientViewModel: ClientViewModel,
    navHostController: NavHostController
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val eventState =
        clientViewModel.eventResultState.collectAsStateWithLifecycle(UiState.Loading)

    LaunchedEffect(eventState.value) {
        clientViewModel.validationCreateEventEvents.collect { event ->
            when (event) {
                is ClientViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context, "Successful registration!", Toast.LENGTH_SHORT
                    ).show()
                    delay(3000L)
                    val result =
                        navHostController.navigate("home_client/$uid") {
                            popUpTo("home_client/$uid") { inclusive = false }
                        }
                }

                else -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            BusinessSecondaryAppBar(
                title = "New Event", navController = navHostController
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
            CreateEventStep2ScreenContent(clientViewModel, navHostController)
        }
    }

}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun CreateEventStep2ScreenContent(
    clientViewModel: ClientViewModel, navController: NavHostController
) {
    val state = clientViewModel.createEventState
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        clientViewModel.validationCreateEventEvents.collect { event ->
            when (event) {
                is ClientViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context, "Offer posted successfully!", Toast.LENGTH_SHORT
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
    Spacer(modifier = Modifier.height(15.dp))
    Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
        Row(horizontalArrangement = Arrangement.Center) {
            Divider(
                modifier = Modifier
                    .width(150.dp)
                    .height(5.dp), color = Rose
            )
            Spacer(modifier = Modifier.width(15.dp))
            Divider(
                modifier = Modifier
                    .width(150.dp)
                    .height(5.dp), color = Rose
            )
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 20.dp, bottom = 0.dp
            ), userScrollEnabled = true,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // VENDORS ITEM
        item {
            var selectedIndex by remember { mutableStateOf(-1) }
            val options = enumValues<BusinessType>()
            val vendorsList by remember {
                mutableStateOf(mutableListOf<String>())
            }
            val selectedIndexes by remember {
                mutableStateOf(mutableListOf<Int>())
            }

            Column() {
                DropdownWithCheckboxesMenu(
                    label = "Vendors",
                    items = options.map { it.name },
                    selectedIndex = selectedIndex,
                    selectedIndexes = selectedIndexes,
                    onItemSelected = { index, businessType ->
                        selectedIndexes.add(index)
                        vendorsList.add(businessType)
                        run {
                            val vendorsString = vendorsList.joinToString(";") { it }
                            //selectedIndex = index
                            clientViewModel.onCreateEventEvent(
                                CreateEventEvent.VendorsChanged(vendorsString)
                            )
                            Log.d("VENDORS", vendorsString)
                        }
                    },
                    onItemDeselected = { index, businessType ->
                        selectedIndexes.remove(index)
                        vendorsList.remove(businessType)
                        run {
                            val vendorsString = vendorsList.joinToString(";") { it }
                            //selectedIndex = index
                            clientViewModel.onCreateEventEvent(
                                CreateEventEvent.VendorsChanged(vendorsString)
                            )
                            Log.d("VENDORS", vendorsString)
                        }
                    },
                    painterResource = R.drawable.baseline_category_24
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = state.vendors.split(";").joinToString(", "), style = Typography.caption)
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
        // GUEST NUMBER + BUDGET ROW ITEM
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // GUEST NUMBER FIELD - row c1
                Column(
                    horizontalAlignment = Alignment.Start, modifier = Modifier.weight(1f)
                ) {
                    Spacer(modifier = Modifier.height(2.dp))
                    FormTextField(
                        labelText = stringResource(R.string.GuestNumber),
                        value = state.guestNumber,
                        onValueChange = {
                            clientViewModel.onCreateEventEvent(
                                CreateEventEvent.GuestNumberChanged(
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
                        trailingIcon = null,
                        keyboardType = KeyboardType.Text
                    )
                    if (state.guestNumberError != null) {
                        ErrorText(text = state.guestNumberError.toString())
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                // BUDGET FIELD - row c2
                Column(
                    horizontalAlignment = Alignment.Start, modifier = Modifier.weight(1f)
                ) {
                    Spacer(modifier = Modifier.height(2.dp))
                    FormTextField(
                        labelText = stringResource(R.string.Budget),
                        value = state.budget,
                        onValueChange = {
                            clientViewModel.onCreateEventEvent(
                                CreateEventEvent.BudgetChanged(
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
                        trailingIcon = null,
                        keyboardType = KeyboardType.Text
                    )
                    if (state.budgetError != null) {
                        ErrorText(text = state.budgetError.toString())
                    }

                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // SUBMIT BUTTON
        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.wrapContentSize(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    border = BorderStroke(
                        width = 1.dp, brush = Brush.horizontalGradient(
                            listOf(
                                CoralAccent,
                                Coral,
                                CoralAccent
                            )
                        )
                    )
                ) {
                    Text(
                        text = stringResource(R.string.Previous),
                        style = Typography.caption,
                        color = Color.DarkGray
                    )
                }
                Spacer(modifier = Modifier.width(30.dp))
                Button(
                    onClick = {
                        clientViewModel.onCreateEventEvent(CreateEventEvent.Submit)
                    },
                    modifier = Modifier.wrapContentSize(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    border = BorderStroke(
                        width = 1.dp, brush = Brush.horizontalGradient(
                            listOf(
                                CoralAccent,
                                Coral,
                                CoralAccent
                            )
                        )
                    )
                ) {
                    Text(
                        text = stringResource(R.string.Submit),
                        style = Typography.caption,
                        color = Color.DarkGray
                    )
                }
            }

        }
    }
}

