package com.example.bachelorthesisapp.presentation.ui.screen.authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.BusinessType
import com.example.bachelorthesisapp.data.model.events.BusinessRegisterEvent
import com.example.bachelorthesisapp.presentation.ui.components.common.BottomClickableText
import com.example.bachelorthesisapp.presentation.ui.components.common.ErrorText
import com.example.bachelorthesisapp.presentation.ui.components.common.FormTextField
import com.example.bachelorthesisapp.presentation.ui.components.common.LargeDropdownMenu
import com.example.bachelorthesisapp.presentation.ui.components.common.SubmitButton
import com.example.bachelorthesisapp.presentation.ui.navigation.Routes
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel

@Composable
fun BusinessRegisterStep1Screen(authViewModel: AuthViewModel, navController: NavHostController) {

    LaunchedEffect(Unit) {
        authViewModel.clearLoginStateForm()
        authViewModel.clearRegisterBusinessStateForm()
        authViewModel.clearRegisterClientStateForm()
    }

    Box {
        Image(
            painter = painterResource(id = R.drawable.login_gradient_background2),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds

        )
        Scaffold(
            backgroundColor = Color.Transparent
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
                val state = authViewModel.registerBusinessState
                val context = LocalContext.current
                LaunchedEffect(key1 = context) {
                    authViewModel.validationBusinessRegisterEvents.collect { event ->
                        when (event) {
                            is AuthViewModel.ValidationEvent.Success -> {
                                Toast.makeText(
                                    context,
                                    "Successful registration!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                //navController.navigate(Routes.BusinessRegisterStep2Screen.route)

                            }

                            else -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Welcome to Event Space! Share your art with the world and be part of the greatness of events.",
                    color = Color.White)
                    Spacer(modifier = Modifier.height(15.dp))
                    BottomClickableText(
                        text = "Already have an account? Sign In here.",
                        onClick = { navController.navigate(Routes.LoginScreen.route) },
                        color = CoralAccent
                    )
                    Spacer(modifier = Modifier.height(37.dp))
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
                        // BUSINESS NAME FIELD
                        item {
                            Column(modifier = Modifier.align(Alignment.Start)) {
                                Spacer(modifier = Modifier.height(2.dp))
                                FormTextField(
                                    labelText = "Business Name",
                                    value = state.name,
                                    onValueChange = {
                                        authViewModel.onBusinessRegisterEvent(
                                            (
                                                    BusinessRegisterEvent.NameChanged(it)
                                                    )
                                        )
                                    },
                                    error = state.nameError,
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_person_24),
                                            contentDescription = ""
                                        )
                                    },
                                    trailingIcon = null,
                                    keyboardType = KeyboardType.Text
                                )
                                if (state.nameError != null) {
                                    ErrorText(text = state.nameError.toString())
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                        }
                        // BUSINESS TYPE FIELD
                        item {
                            var selectedIndex by remember { mutableStateOf(-1) }
                            val options = enumValues<BusinessType>()
                            LargeDropdownMenu(
                                label = "Business Type",
                                items = options.map { it.name },
                                selectedIndex = selectedIndex,
                                onItemSelected = { index, type ->
                                    run {
                                        selectedIndex = index
                                        authViewModel.onBusinessRegisterEvent(
                                            BusinessRegisterEvent.TypeChanged(type)
                                        )
                                    }
                                },
                                painterResource = R.drawable.baseline_category_24
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                        // EMAIL FIELD
                        item {
                            Column(modifier = Modifier.align(Alignment.Start)) {
                                FormTextField(
                                    labelText = "Email",
                                    value = state.email,
                                    onValueChange = {
                                        authViewModel.onBusinessRegisterEvent(
                                            BusinessRegisterEvent.EmailChanged(it)
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
                                        authViewModel.onBusinessRegisterEvent(
                                            BusinessRegisterEvent.PhoneNumberChanged(it)
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
                                        authViewModel.onBusinessRegisterEvent(
                                            BusinessRegisterEvent.PasswordChanged(it)
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
                                        authViewModel.onBusinessRegisterEvent(
                                            BusinessRegisterEvent.ConfirmPasswordChanged(it)
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
                                    authViewModel.onBusinessRegisterEvent(BusinessRegisterEvent.PartialSubmit)
                                    navController.navigate(Routes.BusinessRegisterStep2Screen.route)

                                },
                                text = "Next"
                            )
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }
            }
        }
    }
}
