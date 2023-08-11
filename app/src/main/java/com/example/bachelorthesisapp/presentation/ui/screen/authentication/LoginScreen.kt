package com.example.bachelorthesisapp.presentation.ui.screen.authentication

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.events.LoginEvent
import com.example.bachelorthesisapp.core.resources.Resource
import com.example.bachelorthesisapp.presentation.ui.components.common.BottomClickableText
import com.example.bachelorthesisapp.presentation.ui.components.common.ErrorText
import com.example.bachelorthesisapp.presentation.ui.components.common.FormTextField
import com.example.bachelorthesisapp.presentation.ui.components.common.SubmitButton
import com.example.bachelorthesisapp.presentation.ui.navigation.Routes
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import com.example.bachelorthesisapp.core.presentation.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(authViewModel: AuthViewModel, navHostController: NavHostController) {
    LaunchedEffect(Unit) {
        authViewModel.clearLoginStateForm()
        authViewModel.clearRegisterBusinessStateForm()
        authViewModel.clearRegisterClientStateForm()
    }
    LoginScreenContent(authViewModel, navHostController)
}

@Composable
fun LoginScreenContent(
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    val userType = authViewModel.userTypeState.collectAsStateWithLifecycle(initialValue = "")
    val scope = rememberCoroutineScope()
    val loginState = authViewModel.loginState1.collectAsStateWithLifecycle(UiState.Loading)
    val loginFlow = authViewModel.loginFlow.collectAsState()


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.register_gradient_background),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Scaffold(
            backgroundColor = Color.Transparent
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 155.dp,
                        start = 25.dp,
                        end = 25.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
            ) {
                val loginFormState = authViewModel.loginState
                val context = LocalContext.current
                LaunchedEffect(key1 = context) {
                    authViewModel.validationLoginEvents.collect { event ->
                        when (event) {
                            is AuthViewModel.ValidationEvent.Success -> {
                                Toast.makeText(
                                    context,
                                    "We are logging you in.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                            is AuthViewModel.ValidationEvent.Failure -> Toast.makeText(
                                context,
                                "Error logging in.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    //verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    //verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "EVENT SPACE",
                        style = Typography.h1,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Welcome back!",
                        style = Typography.body2,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(35.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .wrapContentWidth()
                            .height(230.dp)
                            .padding(bottom = 5.dp)
                    ) {
                        Text(
                            text = "Login",
                            color = Color.White,
                            style = Typography.h2

                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        // EMAIL FIELD
                        FormTextField(
                            labelText = "Email",
                            value = loginFormState.email,
                            onValueChange = {
                                scope.launch {
                                    authViewModel.onLoginEvent(
                                        LoginEvent.EmailChanged(
                                            it
                                        )
                                    )
                                }
                            },
                            error = loginFormState.emailError,
                            keyboardType = KeyboardType.Text,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_email_24),
                                    contentDescription = "",
                                    tint = Color.DarkGray
                                )
                            },
                            trailingIcon = null
                        )
                        if (loginFormState.emailError != null) {
                            ErrorText(text = loginFormState.emailError.toString())
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        var passwordVisible by rememberSaveable { mutableStateOf(false) }

                        Spacer(modifier = Modifier.height(2.dp))
                        // PASSWORD FIELD
                        FormTextField(
                            labelText = "Password",
                            value = loginFormState.password,
                            onValueChange = {
                                scope.launch {
                                    authViewModel.onLoginEvent(
                                        LoginEvent.PasswordChanged(
                                            it
                                        )
                                    )
                                }

                            },
                            error = loginFormState.passwordError,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_lock_24),
                                    contentDescription = "",
                                    tint = Color.DarkGray
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
                        if (loginFormState.passwordError != null) {
                            ErrorText(text = loginFormState.passwordError.toString())
                        }
                    }
                    Spacer(modifier = Modifier.height(0.dp))
                    SubmitButton(
                        onClick = {
                            scope.launch {
                                authViewModel.onLoginEvent(LoginEvent.Submit)
                                delay(7000L)
//                                when (val loginContent = loginState.value) {
                                loginFlow.value.let {
                                    when (it) {
                                        is Resource.Loading -> {
                                            Log.d("LOGIN", "LOADING")
                                            Toast.makeText(
                                                context,
                                                "Loadingg...",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        is Resource.Success -> {
                                            Log.d("LOGIN", "SUCCESS")

                                            Toast.makeText(
                                                context,
                                                "Success...",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            delay(2000L)
                                            if (it.data.type == "clients") {
                                                navController.navigate(
                                                    "home_client/${it.data.id}"
                                                )
                                            } else if (it.data.type == "businesses") {
                                                navController.navigate(
                                                    "home_business/${it.data.id}"
                                                )
                                            }
                                        }

                                        is Resource.Error -> {
                                            Log.d("LOGIN", "ERROR")

                                            Toast.makeText(
                                                context,
                                                "Error",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        else -> {}
                                    }
                                }
                            }
                        },
                        text = "Sign In"
                    )
                    Spacer(modifier = Modifier.height(200.dp))
//                    Text(
//                        text = "Or Sign-In with",
//                        style = Typography.subtitle2.copy(color = Color.DarkGray)
//                    )
//                    Spacer(modifier = Modifier.height(19.dp))
//                    // Google, Facebook sign in buttons
//                    Row() {
//                        IconButton(
//                            onClick = { /*TODO*/ },
//                            modifier = Modifier.size(37.dp)
//                        ) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.google_icon),
//                                contentDescription = "Google icon",
//                                tint = Color.Unspecified
//                            )
//                        }
//                        Spacer(modifier = Modifier.width(30.dp))
//                        IconButton(
//                            onClick = { /*TODO*/ },
//                            modifier = Modifier.size(37.dp)
//                        ) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.facebook_logo),
//                                contentDescription = "Facebook icon",
//                                tint = Color.Unspecified
//                            )
//                        }
//                    }
//                    Spacer(modifier = Modifier.height(120.dp))
                    BottomClickableText(
                        text = "New to Event Space? Create an account here.",
                        onClick = { navController.navigate(Routes.MainRegisterScreen.route) },
                        color = Color.Black
                    )
                }
            }

        }
    }
}