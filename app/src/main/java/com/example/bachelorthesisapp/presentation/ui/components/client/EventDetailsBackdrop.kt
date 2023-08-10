package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.Event
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.presentation.ui.theme.IrisBlue
import com.example.bachelorthesisapp.presentation.ui.theme.IrisBlueDark
import com.example.bachelorthesisapp.presentation.ui.theme.IrisBlueLight
import com.example.bachelorthesisapp.presentation.ui.theme.SkyGray
import com.example.bachelorthesisapp.presentation.ui.theme.YogurtWhite
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventDetailsBackdrop(
    event: Event,
    onBusinessTypeFilterClick: (String) -> Unit = {},
    businessState: UiState<List<BusinessEntity>>,
    onBusinessClick: (String) -> Unit = {},
    onCityClicked: (String) -> Unit = {},
    postsList: List<OfferPost>,
    onEditClick: (Int) -> Unit = {},
    onPublishClick: (Int) -> Unit = {}

) {
    BackdropScaffold(modifier = Modifier,
        scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
        frontLayerShape = RoundedCornerShape(30.dp),
        frontLayerScrimColor = Color.Unspecified,
        frontLayerBackgroundColor = SkyGray,
        backLayerBackgroundColor = Color.White,
        peekHeight = 60.dp,
        stickyFrontLayer = false,
        frontLayerElevation = 20.dp,
        headerHeight = 200.dp,
        // Back layer displays the event details info and the business type selection mechanism
        backLayerContent = {
            EventDetailsBackLayerContent(
                event = event,
                onBusinessTypeFilterClick = onBusinessTypeFilterClick,
                postsList = postsList,
                onEditClick = onEditClick,
                onPublishClick = onPublishClick
            )
        },
        appBar = {},
        // Front layer contains the list of the business filtered by the business type selected in the back layer
        frontLayerContent = {
            EventDetailsFrontLayerContent(
                businessState = businessState,
                onBusinessClick = onBusinessClick,
                onCityClicked = onCityClicked,
            )
        })
}