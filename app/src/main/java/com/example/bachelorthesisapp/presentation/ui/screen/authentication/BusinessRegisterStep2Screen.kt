package com.example.bachelorthesisapp.presentation.ui.screen.authentication

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.events.BusinessRegisterEvent
import com.example.bachelorthesisapp.presentation.ui.components.ErrorText
import com.example.bachelorthesisapp.presentation.ui.components.FormTextField
import com.example.bachelorthesisapp.presentation.ui.components.SubmitButton
import com.example.bachelorthesisapp.presentation.ui.navigation.Routes
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.Ochre
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun BusinessRegisterStep2Screen(authVM: AuthViewModel, navController: NavHostController) {


    Box {
//        Image(
//            painter = painterResource(id = R.drawable.main_background),
//            contentDescription = "",
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.FillBounds
//
//        )
        Scaffold(
            backgroundColor = Color.White
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(
                        top = 20.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
            ) {
                val state = authVM.registerBusinessState
                val context = LocalContext.current
                LaunchedEffect(key1 = context) {
                    authVM.validationBusinessRegisterEvents.collect { event ->
                        when (event) {
                            is AuthViewModel.ValidationEvent.Success -> {
                                Toast.makeText(
                                    context,
                                    "Successful registration!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                delay(1500L)
                                navController.navigate(Routes.LoginScreen.route)
                            }

                            else -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                        .align(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Let us know more about your business",
                        style = Typography.h3.copy(color = Coral)
                    )
                    Spacer(modifier = Modifier.height(50.dp))
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
                                    .height(5.dp),
                                color = Rose
                            )
                        }
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 2.dp,
                                bottom = 20.dp
                            ),
                        contentPadding = PaddingValues(vertical = 15.dp),
                        userScrollEnabled = true,
                        verticalArrangement = Arrangement.Center,
                        state = rememberLazyListState(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // CITY FIELD
                        item {
                            Column(modifier = Modifier.align(Alignment.Start)) {
                                Spacer(modifier = Modifier.height(2.dp))
                                FormTextField(
                                    labelText = "City",
                                    value = state.city,
                                    onValueChange = {
                                        authVM.onBusinessRegisterEvent(
                                            (
                                                    BusinessRegisterEvent.CityChanged(it)
                                                    )
                                        )
                                    },
                                    error = state.cityError,
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_location_city_24),
                                            contentDescription = ""
                                        )
                                    },
                                    trailingIcon = null,
                                    keyboardType = KeyboardType.Text
                                )
                                if (state.cityError != null) {
                                    ErrorText(text = state.cityError.toString())
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                        }
                        // ADDRESS FIELD
                        item {
                            Column(modifier = Modifier.align(Alignment.Start)) {
                                FormTextField(
                                    labelText = "Address",
                                    value = state.address,
                                    onValueChange = {
                                        authVM.onBusinessRegisterEvent(
                                            BusinessRegisterEvent.AddressChanged(it)
                                        )
                                    },
                                    error = state.addressError,
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_location_city_24),
                                            contentDescription = ""
                                        )
                                    },
                                    trailingIcon = null,
                                    keyboardType = KeyboardType.Text
                                )
                                Spacer(modifier = Modifier.height(2.dp))

                                if (state.addressError != null) {
                                    ErrorText(text = state.addressError.toString())
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                        item {
                            Text(
                                text = "Optionally, you can provide more details about your location, so that clients can find you easier on the map.",
                                style = Typography.subtitle1
                            )
                        }
                        // LAT-LNG FIELD
                        item {
                            val focusManager = LocalFocusManager.current
                            Spacer(modifier = Modifier.height(12.dp))
                            Row {
                                TextField(
                                    label = { Text(text = "Latitude", color = Color.DarkGray) },
                                    value = state.lat,
                                    modifier = Modifier.width(150.dp),
                                    onValueChange = { authVM.onBusinessRegisterEvent(
                                        BusinessRegisterEvent.LatChanged(it)
                                    ) },
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = Color.Transparent,
                                        focusedLabelColor = Color.Gray,
                                        unfocusedLabelColor = Color.Transparent,
                                        focusedIndicatorColor = Ochre,
                                        unfocusedIndicatorColor = Color.Gray,
                                        cursorColor = Color.Gray
                                    ),
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_my_location_24),
                                            contentDescription = ""
                                        )
                                    },
                                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        imeAction = ImeAction.Done,
                                        keyboardType = KeyboardType.Number
                                    )
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                TextField(
                                    label = { Text(text = "Longitude", color = Color.DarkGray) },
                                    value = state.lng,
                                    modifier = Modifier.width(150.dp),
                                    onValueChange = { authVM.onBusinessRegisterEvent(
                                        BusinessRegisterEvent.LngChanged(it)
                                    )},
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = Color.Transparent,
                                        focusedLabelColor = Color.Gray,
                                        unfocusedLabelColor = Color.Transparent,
                                        focusedIndicatorColor = Ochre,
                                        unfocusedIndicatorColor = Color.Gray,
                                        cursorColor = Color.Gray
                                    ),
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_my_location_24),
                                            contentDescription = ""
                                        )
                                    },
                                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        imeAction = ImeAction.Done,
                                        keyboardType = KeyboardType.Number
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(120.dp))

                        }
                        item {
                            Row() {
                                SubmitButton(
                                    onClick = {
                                        navController.popBackStack()
                                    },
                                    text = "Previous"
                                )
                                Spacer(modifier = Modifier.width(20.dp))
                                SubmitButton(
                                    onClick = {
                                        authVM.onBusinessRegisterEvent(BusinessRegisterEvent.Submit)
                                    },
                                    text = "Sign Up"
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}
