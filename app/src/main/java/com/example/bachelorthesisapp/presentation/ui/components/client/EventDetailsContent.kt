package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.bachelorthesisapp.data.businesses.local.entity.BusinessEntity
import com.example.bachelorthesisapp.domain.model.BusinessType
import com.example.bachelorthesisapp.data.events.local.entity.Event
import com.example.bachelorthesisapp.domain.model.EventStatus
import com.example.bachelorthesisapp.domain.model.EventType
import com.example.bachelorthesisapp.data.posts.local.entity.OfferPost
import com.example.bachelorthesisapp.domain.model.Rating
import com.example.bachelorthesisapp.core.presentation.UiState
import java.time.LocalDate

@Preview(showSystemUi = true)
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
            Pair(BusinessType.CakeShop, 1),
            Pair(BusinessType.Florist, -1),
            Pair(BusinessType.Venue, -1),
            Pair(BusinessType.DecorDesign, 2),
            Pair(BusinessType.Entertainment, -1),
            Pair(BusinessType.Musician, -1),
            Pair(BusinessType.PhotoVideo, 3)
        ),
        EventStatus.Planning
    ),
    businessState: UiState<List<BusinessEntity>> = UiState.Loading,
    onBusinessTypeFilterClick: (String) -> Unit = {},
    onBusinessTypePostClick: () -> Unit = {},
    onBusinessClick: (String) -> Unit = {},
    onCityClicked: (String) -> Unit = {},
    onEditClick: (Int) -> Unit = {},
    onPublishClick: (Int) -> Unit = {},
    onCollaborationCanceledClicked: (Int) -> Unit = {},
    postsList: List<OfferPost> = listOf(
        OfferPost(1, "", "Cake", "", emptyList(), 200, Rating(4.4, 1)),
        OfferPost(2, "", "Decor", "", emptyList(), 200, Rating(4.4, 1)),
        OfferPost(3, "", "Photo", "", emptyList(), 200, Rating(4.4, 1))
    )
) {
    EventDetailsBackdrop(
        event = event,
        onBusinessTypeFilterClick = onBusinessTypeFilterClick,
        businessState = businessState,
        postsList = postsList,
        onBusinessClick = onBusinessClick,
        onCityClicked = onCityClicked,
        onEditClick = onEditClick,
        onPublishClick = onPublishClick,
        onCollaborationCanceledClicked = onCollaborationCanceledClicked
    )
}