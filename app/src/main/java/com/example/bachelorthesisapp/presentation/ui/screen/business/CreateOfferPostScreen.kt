package com.example.bachelorthesisapp.presentation.ui.screen.business

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.data.model.events.CreatePostEvent
import com.example.bachelorthesisapp.presentation.ui.components.BusinessSecondaryAppBar
import com.example.bachelorthesisapp.presentation.ui.components.ErrorText
import com.example.bachelorthesisapp.presentation.ui.components.FormTextField
import com.example.bachelorthesisapp.presentation.ui.components.SmallSubmitButton
import com.example.bachelorthesisapp.presentation.ui.components.SubmitButton
import com.example.bachelorthesisapp.presentation.ui.theme.IrisBlueDark
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.viewmodel.BusinessViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState

@Composable
fun CreateOfferPostScreen(
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
            topBar = { BusinessSecondaryAppBar(title = "New Post", navController = navController) },
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
            SubmitButton(
                onClick = { businessViewModel.onCreatePostEvent(CreatePostEvent.Submit) },
                text = stringResource(R.string.Submit)
            )
        }
    }
}

@SuppressLint("MutableCollectionMutableState")

@Composable
fun GalleryImagePicker(onValueChanged: (String) -> Unit) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val imagesList by remember {
        mutableStateOf<MutableList<Uri?>>(mutableListOf())
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        Log.d("URI", "$uri")
        if (imageUri !in imagesList) {
            imagesList.add(imageUri)
            val imagesString = imagesList.joinToString(";") { it.toString() }
            onValueChanged(imagesString)
        }
    }

    Column(horizontalAlignment = Alignment.Start) {
        Row(modifier = Modifier.padding(start = 10.dp)) {
            Text(
                modifier = Modifier,
                text = stringResource(R.string.Images),
                color = Color.DarkGray,
                style = Typography.subtitle1
            )
            Spacer(modifier = Modifier.width(20.dp))
            SmallSubmitButton(
                onClick = { launcher.launch("image/*") },
                text = stringResource(id = R.string.Upload)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow() {
            items(imagesList.size) { imageIndex ->
                val imgUri = imagesList[imageIndex]
                imgUri?.let {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)

                    bitmap.value?.let { btm ->
                        Card(
                            modifier = Modifier
                                .size(130.dp)
                                .padding(start = 6.dp, end = 6.dp)
                        ) {
                            Image(
                                bitmap = btm.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

    }

}