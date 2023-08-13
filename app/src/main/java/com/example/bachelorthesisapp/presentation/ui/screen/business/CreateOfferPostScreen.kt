package com.example.bachelorthesisapp.presentation.ui.screen.business

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.events.CreatePostEvent
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessSecondaryAppBar
import com.example.bachelorthesisapp.presentation.ui.components.common.ErrorText
import com.example.bachelorthesisapp.presentation.ui.components.common.FormTextField
import com.example.bachelorthesisapp.presentation.ui.components.common.GalleryImagePicker
import com.example.bachelorthesisapp.presentation.ui.components.common.SubmitButton
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.viewmodel.BusinessViewModel

@Composable
fun CreateOfferPostScreen(
    navController: NavHostController,
    businessViewModel: BusinessViewModel
) {
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(Unit) {
        businessViewModel.clearCreatePostForm()
    }

    Box {
//        Image(
//            painter = painterResource(id = R.drawable.register_gradient_background),
//            contentDescription = "background",
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.FillBounds
//        )
        Scaffold(
            topBar = {
                BusinessSecondaryAppBar(
                    title = "New Post",
                    navController = navController,
                    backgroundColor = Coral,
                    elevation = 10.dp
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
                CreateOfferPostScreenContent(businessViewModel, navController)
            }
        }
    }
}

@Composable
fun CreateOfferPostScreenContent(
    businessViewModel: BusinessViewModel,
    navController: NavHostController
) {
    val state = businessViewModel.createPostState
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        businessViewModel.validationCreatePostEvents.collect { event ->
            when (event) {
                is BusinessViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Offer posted successfully!",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    navController.popBackStack()
                }

                is BusinessViewModel.ValidationEvent.Failure -> {
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 3.dp,
                bottom = 0.dp
            ),
        userScrollEnabled = true,
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
                        businessViewModel.onCreatePostEvent(
                            CreatePostEvent.TitleChanged(
                                it
                            )
                        )
                    },
                    error = state.titleError,
                    leadingIcon = null,
                    trailingIcon = null,
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
                        businessViewModel.onCreatePostEvent(
                            CreatePostEvent.DescriptionChanged(
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
        // PRICE ITEM
        item {
            Column(horizontalAlignment = Alignment.Start) {
                Spacer(modifier = Modifier.height(2.dp))
                FormTextField(
                    labelText = stringResource(R.string.Price),
                    value = state.price,
                    onValueChange = {
                        businessViewModel.onCreatePostEvent(
                            CreatePostEvent.PriceChanged(
                                it
                            )
                        )
                    },
                    error = state.priceError,
                    leadingIcon = null,
                    trailingIcon = null,
                    keyboardType = KeyboardType.Number
                )
                if (state.priceError != null) {
                    ErrorText(text = state.priceError.toString())
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        // IMAGES PICKER ITEM
        item {
            GalleryImagePicker {
                businessViewModel.onCreatePostEvent(
                    CreatePostEvent.ImagesChanged(
                        it
                    )
                )
            }
            if (state.imagesError != null) {
                ErrorText(text = state.imagesError.toString())
            }
            Spacer(modifier = Modifier.padding(5.dp))
        }
        // SUBMIT BUTTON
        item {
            Button(
                onClick = { businessViewModel.onCreatePostEvent(CreatePostEvent.Submit) },
                modifier = Modifier.wrapContentSize(),
                colors = ButtonDefaults.buttonColors(backgroundColor = CoralAccent)
            ) {
                Text(
                    text = stringResource(R.string.Submit),
                    style = Typography.caption,
                    color = Color.White
                )
            }
        }
    }
}
