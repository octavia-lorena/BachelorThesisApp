package com.example.bachelorthesisapp.presentation.ui.components.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarType
import java.time.LocalDate

@Preview
@Composable
fun PreviewKalendar(){
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    Kalendar(
        currentDay = kotlinx.datetime.LocalDate(2023,8,10),
        kalendarType = KalendarType.Firey
    )
}