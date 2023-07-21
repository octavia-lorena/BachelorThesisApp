package com.example.bachelorthesisapp.presentation.ui.screen.authentication

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.events.ClientRegisterEvent
import com.example.bachelorthesisapp.presentation.ui.components.BottomClickableText
import com.example.bachelorthesisapp.presentation.ui.components.ErrorText
import com.example.bachelorthesisapp.presentation.ui.components.FormTextField
import com.example.bachelorthesisapp.presentation.ui.components.SubmitButton
import com.example.bachelorthesisapp.presentation.ui.navigation.Routes
import com.example.bachelorthesisapp.presentation.ui.theme.NavyBlue
import com.example.bachelorthesisapp.presentation.ui.theme.OffWhite
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun ClientRegisterScreen(authVM: AuthViewModel, navController: NavHostController) {


    Box {
//        Image(
//            painter = painterResource(id = R.drawable.main_background),
//            contentDescription = "",
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.FillBounds
//
//        )
        Scaffold(
            backgroundColor = OffWhite
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    //.fillMaxWidth()
                    .wrapContentSize()
                    .padding(
                        top = 40.dp,
                        start = 25.dp,
                        end = 25.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
            ) {
                val state = authVM.registerClientState
                val context = LocalContext.current
                LaunchedEffect(key1 = context) {
                    authVM.validationClientRegisterEvents.collect { event ->
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
                    Text(text = "Welcome to Event Space! Unleash your creativity and organize the event of your dreams.")
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 10.dp,
                                bottom = 20.dp
                            ),
                        contentPadding = PaddingValues(vertical = 20.dp),
                        userScrollEnabled = true,
                        verticalArrangement = Arrangement.Center,
                        state = rememberLazyListState(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // FIRST NAME FIELD
                        item {
                            Column(modifier = Modifier.align(Alignment.Start)) {
                                Spacer(modifier = Modifier.height(2.dp))
                                FormTextField(
                                    labelText = "First Name",
                                    value = state.firstName,
                                    onValueChange = {
                                        authVM.onClientRegisterEvent(
                                            (
                                                    ClientRegisterEvent.FirstNameChanged(it)
                                                    )
                                        )
                                    },
                                    error = state.firstNameError,
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_person_24),
                                            contentDescription = ""
                                        )
                                    },
                                    trailingIcon = null,
                                    keyboardType = KeyboardType.Text
                                )
                                if (state.firstNameError != null) {
                                    ErrorText(text = state.firstNameError.toString())
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                        }
                        // LAST NAME FIELD
                        item {
                            Column(modifier = Modifier.align(Alignment.Start)) {
                                Spacer(modifier = Modifier.height(2.dp))
                                FormTextField(
                                    labelText = "Last Name",
                                    value = state.lastName,
                                    onValueChange = {
                                        authVM.onClientRegisterEvent(
                                            ClientRegisterEvent.LastNameChanged(
                                                it
                                            )
                                        )
                                    },
                                    error = state.lastNameError,
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_person_24),
                                            contentDescription = ""
                                        )
                                    },
                                    trailingIcon = null,
                                    keyboardType = KeyboardType.Text
                                )
                                if (state.lastNameError != null) {
                                    ErrorText(text = state.lastNameError.toString())
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                        // EMAIL FIELD
                        item {
                            Column(modifier = Modifier.align(Alignment.Start)) {
                                FormTextField(
                                    labelText = "Email",
                                    value = state.email,
                                    onValueChange = {
                                        authVM.onClientRegisterEvent(
                                            ClientRegisterEvent.EmailChanged(it)
                                        )
                                    },
                                    error = state.emailError,
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_email_24),
                                            contentDescription = ""
                                        )
                                    },
                                    trailingIcon = null,
                                    keyboardType = KeyboardType.Text
                                )
                                Spacer(modifier = Modifier.height(2.dp))

                                if (state.emailError != null) {
                                    ErrorText(text = state.emailError.toString())
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                        // PHONE NUMBER FIELD
                        item {
                            Column(modifier = Modifier.align(Alignment.Start)) {
                                Spacer(modifier = Modifier.height(2.dp))
                                FormTextField(
                                    labelText = "Phone Number",
                                    value = state.phoneNumber,
                                    onValueChange = {
                                        authVM.onClientRegisterEvent(
                                            ClientRegisterEvent.PhoneNumberChanged(it)
                                        )
                                    },
                                    error = state.phoneNumberError,
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_phone_24),
                                            contentDescription = ""
                                        )
                                    },
                                    trailingIcon = null,
                                    keyboardType = KeyboardType.Number
                                )
                                if (state.phoneNumberError != null) {
                                    ErrorText(text = state.phoneNumberError.toString())
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                        // PASSWORD FIELD
                        item {
                            Column(modifier = Modifier.align(Alignment.Start)) {
                                var passwordVisible by rememberSaveable { mutableStateOf(false) }
                                Spacer(modifier = Modifier.height(2.dp))
                                FormTextField(
                                    labelText = "Password",
                                    value = state.password,
                                    onValueChange = {
                                        authVM.onClientRegisterEvent(
                                            ClientRegisterEvent.PasswordChanged(it)
                                        )
                                    },
                                    error = state.passwordError,
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_lock_24),
                                            contentDescription = ""
                                        )
                                    },
                                    trailingIcon = {
                                        val image = if (passwordVisible)
                                            Icons.Filled.Visibility
                                        else Icons.Filled.VisibilityOff

                                        // Please provide localized description for accessibility services
                                        val description =
                                            if (passwordVisible) "Hide password" else "Show password"

                                        IconButton(onClick = {
                                            passwordVisible = !passwordVisible
                                        }) {
                                            Icon(imageVector = image, description)
                                        }
                                    },
                                    keyboardType = KeyboardType.Password,
                                    passwordVisible = passwordVisible
                                )
                                if (state.passwordError != null) {
                                    ErrorText(text = state.passwordError.toString())
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                        // CONFIRM PASSWORD FIELD
                        item {
                            Column(modifier = Modifier.align(Alignment.Start)) {
                                var passwordVisible by rememberSaveable { mutableStateOf(false) }
                                Spacer(modifier = Modifier.height(2.dp))
                                FormTextField(
                                    labelText = "Confirm Password",
                                    value = state.confirmPassword,
                                    onValueChange = {
                                        authVM.onClientRegisterEvent(
                                            ClientRegisterEvent.ConfirmPasswordChanged(
                                                it
                                            )
                                        )
                                    },
                                    error = state.confirmPasswordError,
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_lock_24),
                                            contentDescription = ""
                                        )
                                    },
                                    trailingIcon = {
                                        val image = if (passwordVisible)
                                            Icons.Filled.Visibility
                                        else Icons.Filled.VisibilityOff

                                        val description =
                                            if (passwordVisible) "Hide password" else "Show password"

                                        IconButton(onClick = {
                                            passwordVisible = !passwordVisible
                                        }) {
                                            Icon(imageVector = image, description)
                                        }
                                    },
                                    keyboardType = KeyboardType.Password,
                                    passwordVisible = passwordVisible
                                )
                                if (state.confirmPasswordError != null) {
                                    ErrorText(text = state.confirmPasswordError.toString())
                                }
                                Spacer(modifier = Modifier.height(25.dp))
                            }
                        }
                        item {
                            SubmitButton(
                                onClick = {
                                    authVM.onClientRegisterEvent(ClientRegisterEvent.Submit)
                                },
                                text = "Sign Up"
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                            BottomClickableText(
                                text = "Already have an account? Sign In here.",
                                onClick = { navController.navigate(Routes.LoginScreen.route) },
                                color = NavyBlue
                            )
                        }
                    }
                }
            }
        }
    }
}
