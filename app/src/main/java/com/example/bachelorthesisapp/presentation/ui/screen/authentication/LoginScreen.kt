package com.example.bachelorthesisapp.presentation.ui.screen.authentication

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntSize
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
    val scope = rememberCoroutineScope()
    val loginState = authViewModel.loginState1.collectAsStateWithLifecycle(UiState.Loading)
    val isLoading = authViewModel.isLoading.collectAsState()
    var _isLoading by remember {
        mutableStateOf(false)
    }


    val context = LocalContext.current

    LaunchedEffect(key1 = isLoading.value) {
        _isLoading = isLoading.value
    }

    LaunchedEffect(key1 = loginState.value, key2 = Unit) {
        Log.d("LOGIN", "STATE: ${loginState.value}")
        when (val loginContent = loginState.value) {
            is UiState.Success -> {
                Toast.makeText(
                    context,
                    "Success",
                    Toast.LENGTH_SHORT
                )
                    .show()
                if (loginContent.value.type == "clients") {
                    navController.navigate(
                        "home_client/${loginContent.value.id}"
                    )
                } else if (loginContent.value.type == "businesses") {
                    navController.navigate(
                        "home_business/${loginContent.value.id}"
                    )
                }
            }

            is UiState.Loading -> {
                Toast.makeText(
                    context,
                    "Loading...",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

            is UiState.Error -> {
                Toast.makeText(
                    context,
                    "Error",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
//        authViewModel.validationLoginEvents.collect { event ->
//            when (event) {
//                is AuthViewModel.ValidationEvent.Success -> {
//                    _isLoading = false
//                    Toast.makeText(
//                        context,
//                        "We are logging you in.",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//
//                    val loginContent = loginState.value
//                    if (loginContent is UiState.Success) {
//                        if (loginContent.value.type == "clients") {
//                            navController.navigate(
//                                "home_client/${loginContent.value.id}"
//                            )
//                        } else if (loginContent.value.type == "businesses") {
//                            navController.navigate(
//                                "home_business/${loginContent.value.id}"
//                            )
//                        }
//                    } else {
//                        Toast.makeText(
//                            context,
//                            "Error/Loading.",
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                    }
//                }
//
//                is AuthViewModel.ValidationEvent.Failure -> {
//                    _isLoading = true
//                    Toast.makeText(
//                        context,
//                        "Error logging in.",
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()
//                }
//            }
//        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_logo),
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
                        top = 210.dp,
                        start = 25.dp,
                        end = 25.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
            ) {
                val loginFormState = authViewModel.loginState
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Welcome back!",
                        style = Typography.body2,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(60.dp))
                    Column(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .wrapContentWidth()
                            .height(170.dp)
                            .padding(bottom = 5.dp)
                    ) {
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
                    SubmitButton(
                        onClick = {
                            scope.launch {
                                authViewModel.onLoginEvent(LoginEvent.Submit)
                                delay(3000L)
                            }
                        },
                        text = "Sign In",
                        isLoading = _isLoading
                    )
                    Spacer(modifier = Modifier.height(220.dp))
                    BottomClickableText(
                        text = "New to Event Space? Create an account here.",
                        onClick = { navController.navigate(Routes.MainRegisterScreen.route) },
                        color = Color.Black,
                    )
                }
            }

        }
    }
}
