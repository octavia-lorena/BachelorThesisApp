package com.example.bachelorthesisapp.presentation.ui.components.client

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.Cities
import com.example.bachelorthesisapp.data.model.entities.OfferPost
import com.example.bachelorthesisapp.presentation.ui.components.common.LargeDropdownMenu
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.SkyGrayDark
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun EventDetailsFrontLayerContent(
    businessState: UiState<List<BusinessEntity>> = UiState.Loading,
    onBusinessClick: (String) -> Unit = {},
    onCityClicked: (String) -> Unit = {},
) {
    val citiesList = enumValues<Cities>().toList().map { it.name }
    val items = mutableListOf("-")
    citiesList.forEach { items.add(it) }
    var selectedCity by remember {
        mutableStateOf("-")
    }
    var selectedIndex by remember { mutableStateOf(-1) }

    LazyColumn(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(6.dp)
                    .background(color = SkyGrayDark, shape = RoundedCornerShape(10.dp))
            )
        }
        item {
            LargeDropdownMenu(
                label = "City", items = items,
                onItemSelected = { index, item ->
                    selectedIndex = index
                    selectedCity = item
                    onCityClicked(selectedCity)
                },
                painterResource = R.drawable.baseline_location_city_24,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                selectedIndex = selectedIndex,
                textColor = Color.White,
                iconColor = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        when (businessState) {
            is UiState.Loading -> item {
                CircularProgressIndicator(
                    modifier = Modifier.padding(20.dp),
                    backgroundColor = Coral,
                    color = CoralLight
                )
            }

            is UiState.Success -> {
                // Display the list of businesses filtered by type
                var businessList = businessState.value
                // Filter by city if a city is selected from the dropdown
                if (selectedCity != "-")
                    businessList = businessList.filter { it.city == selectedCity }
                items(businessList.size) { index ->
                    val business = businessList[index]
                    EventDetailsBusinessCard(business = business, onBusinessClick = onBusinessClick)
                }
            }

            is UiState.Error -> item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Could not load the page.",
                    color = Color.White
                )
            }
        }
    }
}