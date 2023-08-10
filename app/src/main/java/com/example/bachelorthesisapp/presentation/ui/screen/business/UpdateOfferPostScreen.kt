package com.example.bachelorthesisapp.presentation.ui.screen.business

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.events.UpdatePostEvent
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessSecondaryAppBar
import com.example.bachelorthesisapp.presentation.ui.components.common.ErrorText
import com.example.bachelorthesisapp.presentation.ui.components.common.FormTextField
import com.example.bachelorthesisapp.presentation.ui.components.common.GalleryImagePicker
import com.example.bachelorthesisapp.presentation.ui.components.common.SubmitButton
import com.example.bachelorthesisapp.presentation.viewmodel.BusinessViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState

@Composable
fun UpdateOfferPostScreen(
    postId: Int,
    navController: NavHostController,
    businessViewModel: BusinessViewModel
) {
    val scaffoldState = rememberScaffoldState()

    Box {
        Image(
            painter = painterResource(id = R.drawable.create_post_background),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Scaffold(
            topBar = { BusinessSecondaryAppBar(title = "Edit Post Details", navController = navController) },
            scaffoldState = scaffoldState,
//        drawerContent = {
//            BusinessDrawerContent(uid, authVM, navController)
//        },
            drawerGesturesEnabled = true,
            backgroundColor = Color.Transparent
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
                UpdateOfferPostScreenContent(postId, businessViewModel, navController)
            }
        }
    }
}

@Composable
fun UpdateOfferPostScreenContent(
    postId: Int,
    businessViewModel: BusinessViewModel,
    navController: NavHostController
) {
    val state = businessViewModel.updatePostState
    val context = LocalContext.current

    state.id = postId

    LaunchedEffect(key1 = context) {
        businessViewModel.validationUpdatePostEvents.collect { event ->
            when (event) {
                is BusinessViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Offer updated successfully!",
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
                        businessViewModel.onUpdatePostEvent(
                            UpdatePostEvent.TitleChanged(
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
                        businessViewModel.onUpdatePostEvent(
                            UpdatePostEvent.DescriptionChanged(
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
                        businessViewModel.onUpdatePostEvent(
                            UpdatePostEvent.PriceChanged(
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
                businessViewModel.onUpdatePostEvent(
                    UpdatePostEvent.ImagesChanged(
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
            SubmitButton(
                onClick = { businessViewModel.onUpdatePostEvent(UpdatePostEvent.Submit) },
                text = stringResource(R.string.Submit)
            )
        }
    }
}
