package com.example.bachelorthesisapp.presentation.ui.screen.business

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.data.model.events.UpdatePostEvent
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessSecondaryAppBar
import com.example.bachelorthesisapp.presentation.ui.components.common.ErrorText
import com.example.bachelorthesisapp.presentation.ui.components.common.FormTextField
import com.example.bachelorthesisapp.presentation.ui.components.common.GalleryImagePicker
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.viewmodel.BusinessViewModel

@Composable
fun UpdateOfferPostScreen(
    postId: Int,
    navController: NavHostController,
    businessViewModel: BusinessViewModel
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        topBar = {
            BusinessSecondaryAppBar(
                title = "Edit Post Details",
                navController = navController,
                backgroundColor = Coral,
                elevation = 10.dp,
                onNavBackClick = {
                    businessViewModel.clearUpdatePostForm()
                }
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
            UpdateOfferPostScreenContent(postId, businessViewModel, navController)
        }
    }
}

@Composable
fun UpdateOfferPostScreenContent(
    postId: Int,
    businessViewModel: BusinessViewModel,
    navController: NavHostController
) {
    val updatePostFormState = businessViewModel.updatePostState
    val postResultState =
        businessViewModel.postResponseState.collectAsStateWithLifecycle(initialValue = UiState.Loading)
    val context = LocalContext.current

    updatePostFormState.id = postId

    LaunchedEffect(key1 = postResultState.value) {
        when (postResultState.value) {
            is UiState.Success -> {
                Toast.makeText(
                    context,
                    "Offer updated successfully!",
                    Toast.LENGTH_SHORT
                )
                    .show()
                navController.popBackStack()
            }

            is UiState.Loading ->
                Toast.makeText(
                    context, "Loading...", Toast.LENGTH_SHORT
                ).show()


            is UiState.Error -> Toast.makeText(
                context, "Error updating the offer post..", Toast.LENGTH_SHORT
            ).show()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 10.dp,
                bottom = 0.dp
            ),
        userScrollEnabled = true,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TITLE ITEM
        item {
            Column(horizontalAlignment = Alignment.Start) {
                Spacer(modifier = Modifier.height(2.dp))
                FormTextField(
                    labelText = stringResource(R.string.Title),
                    value = updatePostFormState.title,
                    onValueChange = {
                        businessViewModel.onUpdatePostEvent(
                            UpdatePostEvent.TitleChanged(
                                it
                            )
                        )
                    },
                    error = updatePostFormState.titleError,
                    leadingIcon = null,
                    trailingIcon = null,
                    keyboardType = KeyboardType.Text
                )
                if (updatePostFormState.titleError != null) {
                    ErrorText(text = updatePostFormState.titleError.toString())
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
                    value = updatePostFormState.description,
                    onValueChange = {
                        businessViewModel.onUpdatePostEvent(
                            UpdatePostEvent.DescriptionChanged(
                                it
                            )
                        )
                    },
                    error = updatePostFormState.descriptionError,
                    leadingIcon = null,
                    trailingIcon = null,
                    keyboardType = KeyboardType.Text
                )
                if (updatePostFormState.descriptionError != null) {
                    ErrorText(text = updatePostFormState.descriptionError.toString())
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
                    value = updatePostFormState.price,
                    onValueChange = {
                        businessViewModel.onUpdatePostEvent(
                            UpdatePostEvent.PriceChanged(
                                it
                            )
                        )
                    },
                    error = updatePostFormState.priceError,
                    leadingIcon = null,
                    trailingIcon = null,
                    keyboardType = KeyboardType.Number
                )
                if (updatePostFormState.priceError != null) {
                    ErrorText(text = updatePostFormState.priceError.toString())
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        // IMAGES PICKER ITEM
        item {
            GalleryImagePicker(
                initialValues = updatePostFormState.images.split(";")
            ) {
                businessViewModel.onUpdatePostEvent(
                    UpdatePostEvent.ImagesChanged(
                        it
                    )
                )
            }
            if (updatePostFormState.imagesError != null) {
                ErrorText(text = updatePostFormState.imagesError.toString())
            }
            Spacer(modifier = Modifier.padding(5.dp))
        }
        // SUBMIT BUTTON
        item {
            Button(
                onClick = { businessViewModel.onUpdatePostEvent(UpdatePostEvent.Submit) },
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
                    text = stringResource(R.string.Update),
                    style = Typography.caption,
                    color = Color.DarkGray
                )
            }
        }
    }
}
