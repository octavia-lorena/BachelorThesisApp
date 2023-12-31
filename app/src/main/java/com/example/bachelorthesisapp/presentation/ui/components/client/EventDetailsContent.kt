package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.data.model.BusinessType
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.data.model.EventStatus
import com.example.bachelorthesisapp.data.model.EventType
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.data.model.Rating
import com.example.bachelorthesisapp.core.presentation.UiState
import java.time.LocalDate

@Composable
fun EventDetailsScreenContent(
    event: Event = Event(
        1,
        "",
        "A+B Wedding",
        "A classy wedding, bla bla, family and friends, love and marriage bla bla",
        EventType.Wedding,
        LocalDate.parse("2023-07-25"),
        "13:30",
        100,
        1000,
        500,
        mapOf(
            Pair(BusinessType.Beauty, -1),
            Pair(BusinessType.Catering, 1),
            Pair(BusinessType.Florist, -1),
            Pair(BusinessType.Venue, -1),
            Pair(BusinessType.DecorDesign, 2),
            Pair(BusinessType.Entertainment, -1),
            Pair(BusinessType.Music, -1),
            Pair(BusinessType.PhotoVideo, 3)
        ),
        EventStatus.Planning
    ),
    businessState: UiState<List<BusinessEntity>> = UiState.Loading,
    businessType: String = "",
    onBusinessTypeFilterClick: (String) -> Unit = {},
    onBusinessTypePostClick: () -> Unit = {},
    onBusinessClick: (String) -> Unit = {},
    onCityClicked: (String) -> Unit = {},
    onEditClick: (Int) -> Unit = {},
    onPublishClick: (Int) -> Unit = {},
    onCollaborationCanceledClicked: (Int) -> Unit = {},
    postsList: UiState<List<OfferPost>>,
) {
    EventDetailsBackdrop(
        event = event,
        onBusinessTypeFilterClick = onBusinessTypeFilterClick,
        businessState = businessState,
        postsList = postsList,
        businessType = businessType,
        onBusinessClick = onBusinessClick,
        onCityClicked = onCityClicked,
        onEditClick = onEditClick,
        onPublishClick = onPublishClick,
        onCollaborationCanceledClicked = onCollaborationCanceledClicked
    )
}