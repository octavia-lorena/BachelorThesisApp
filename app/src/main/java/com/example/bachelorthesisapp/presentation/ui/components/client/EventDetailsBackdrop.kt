package com.example.bachelorthesisapp.presentation.ui.components.client

import android.widget.Toast
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.presentation.ui.theme.SkyGray
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.presentation.ui.theme.OffWhite

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventDetailsBackdrop(
    event: Event,
    onBusinessTypeFilterClick: (String) -> Unit = {},
    businessState: UiState<List<BusinessEntity>>,
    onBusinessClick: (String) -> Unit = {},
    onCityClicked: (String) -> Unit = {},
    postsList: UiState<List<OfferPost>>,
    businessType: String,
    onEditClick: (Int) -> Unit = {},
    onPublishClick: (Int) -> Unit = {},
    onCollaborationCanceledClicked: (Int) -> Unit = {}
) {
    val context = LocalContext.current

    BackdropScaffold(modifier = Modifier,
        scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
        frontLayerShape = RoundedCornerShape(30.dp),
        frontLayerScrimColor = Color.Unspecified,
        frontLayerBackgroundColor = OffWhite,
        backLayerBackgroundColor = Color.White,
        peekHeight = 60.dp,
        stickyFrontLayer = false,
        frontLayerElevation = 20.dp,
        headerHeight = 150.dp,
        // Back layer displays the event details info and the business type selection mechanism
        backLayerContent = {
            when (postsList) {
                is UiState.Success -> {
                    val posts = postsList.value
                    EventDetailsBackLayerContent(
                        event = event,
                        onBusinessTypeFilterClick = onBusinessTypeFilterClick,
                        postsList = posts,
                        onEditClick = onEditClick,
                        onPublishClick = onPublishClick,
                        onCollaborationCanceledClicked = onCollaborationCanceledClicked
                    )
                }

                is UiState.Error -> {
                    Toast.makeText(context, "Error loading posts", Toast.LENGTH_SHORT).show()
                }

                is UiState.Loading -> {
                    Toast.makeText(context, "Loading posts...", Toast.LENGTH_SHORT).show()

                }
            }

        },
        appBar = {},
        // Front layer contains the list of the business filtered by the business type selected in the back layer
        frontLayerContent = {
            when (postsList) {
                is UiState.Success -> {
                    val posts = postsList.value
                    EventDetailsFrontLayerContent(
                        businessState = businessState,
                        onBusinessClick = onBusinessClick,
                        onCityClicked = onCityClicked,
                        businessType = businessType,
                        postsList = posts
                    )
                }

                is UiState.Error -> {
                    Toast.makeText(context, "Error loading posts", Toast.LENGTH_SHORT).show()
                }

                is UiState.Loading -> {
                    Toast.makeText(context, "Loading posts...", Toast.LENGTH_SHORT).show()

                }
            }

        })
}