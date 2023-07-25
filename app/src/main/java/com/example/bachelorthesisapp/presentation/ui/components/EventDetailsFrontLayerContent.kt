package com.example.bachelorthesisapp.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.data.model.entities.BusinessEntity
import com.example.bachelorthesisapp.data.model.entities.Cities

@Preview
@Composable
fun EventDetailsFrontLayerContent(
    businessesList: List<BusinessEntity> = listOf()
) {
    val citiesList = enumValues<Cities>().toList()
    var selectedCity by remember {
        mutableStateOf("")
    }
    var selectedIndex by remember { mutableStateOf(-1) }


    LazyColumn(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            LargeDropdownMenu(
                label = selectedCity, items = citiesList,
                onItemSelected = { index, item ->
                    selectedIndex = index
                    selectedCity = item.name
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
        items(businessesList.size) { index ->
            val business = businessesList[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(5.dp)
            ) {
                Text(text = business.businessName)

            }
        }
    }
}