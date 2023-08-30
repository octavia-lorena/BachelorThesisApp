package com.example.bachelorthesisapp.presentation.ui.screen.authentication

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import coil.compose.AsyncImage
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.events.BusinessRegisterEvent
import com.example.bachelorthesisapp.presentation.ui.components.common.ErrorText
import com.example.bachelorthesisapp.presentation.ui.components.common.FormTextField
import com.example.bachelorthesisapp.presentation.ui.components.common.SmallSubmitButton
import com.example.bachelorthesisapp.presentation.ui.components.common.SubmitButton
import com.example.bachelorthesisapp.presentation.ui.navigation.Routes
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.Ochre
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun BusinessRegisterStep2Screen(authVM: AuthViewModel, navController: NavHostController) {

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
                                authVM.clearRegisterBusinessStateForm()
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
                        style = Typography.h3.copy(color = CoralAccent)
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
                                text = "Optionally, upload a profile picture.",
                                style = Typography.subtitle1,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                        item {
                            var imageUrl by remember {
                                mutableStateOf("")
                            }
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.GetContent()
                            ) { uri: Uri? ->
                                imageUrl = uri.toString()
                            }
                            LaunchedEffect(key1 = imageUrl) {
                                authVM.onBusinessRegisterEvent(
                                    BusinessRegisterEvent.ProfilePictureChanged(imageUrl)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(90.dp)
                                        .padding(top = 0.dp)
                                        .background(Color.LightGray, shape = CircleShape)
                                        .border(
                                            width = 1.dp,
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color.DarkGray,
                                                    Color.Gray,
                                                    Color.White
                                                )
                                            ),
                                            shape = RoundedCornerShape(50.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape),
                                        model = if (imageUrl != "") imageUrl else null,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        placeholder = painterResource(id = R.drawable.profile_picture_placeholder)
                                    )
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                SmallSubmitButton(
                                    onClick = {
                                        launcher.launch("image/*")
                                    },
                                    text = stringResource(id = R.string.Upload),
                                    backgroundColor = Color.LightGray
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
