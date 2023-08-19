package com.example.bachelorthesisapp.presentation.ui.components.common

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerColors
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate

@Composable
fun DropdownDateMenu(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    onItemSelected: (item: String) -> Unit,
    busyDates: List<String> = emptyList(),
    initialDate: String = "Pick a date"
) {
    var expanded by remember { mutableStateOf(false) }
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    val dateDialogState = rememberMaterialDialogState()
    var dateValue by remember {
        mutableStateOf(initialDate)
    }

    Box(modifier = modifier.height(IntrinsicSize.Min)) {
        TextField(
            label = { Text(text = label, style = Typography.caption) },
            value = dateValue,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                    contentDescription = "",
                    tint = Color.Gray
                )
            },
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, "")
            },
            onValueChange = {
                dateValue = it
                Log.d("TYPE_SELECTED", it)
            },
            readOnly = true,
            textStyle = Typography.h6,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedLabelColor = Color.Gray,
                unfocusedLabelColor = Color.Transparent,
                focusedIndicatorColor = CoralAccent,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = Color.Gray,
                textColor = Color.Black
            ),
        )

        // Transparent clickable surface on top of TextField
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(MaterialTheme.shapes.small)
                .clickable(enabled = enabled, onClick = {
                    expanded = true
                    dateDialogState.show()
                }),
            color = Color.Transparent,
        ) {}
    }

    if (expanded) {
        MaterialDialog(
            dialogState = dateDialogState,
            backgroundColor = Rose,
            buttons = {
                positiveButton(text = "Ok") {
                    onItemSelected(pickedDate.toString())
                    dateValue = pickedDate.toString()
                    expanded = false
                }
                negativeButton(text = "Cancel") {
                    onItemSelected(pickedDate.toString())
                    expanded = false
                }
            },
            autoDismiss = true
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
            ) {
                datepicker(
                    initialDate = LocalDate.now(),
                    title = "Pick a date",
                    allowedDateValidator = { it.toString() !in busyDates },
                    colors = DatePickerDefaults.colors(
                        headerBackgroundColor = Rose,
                        headerTextColor = Color.White,
                        calendarHeaderTextColor = Rose,
                        dateActiveBackgroundColor = CoralAccent
                    )
                ) {
                    pickedDate = it

                }
            }

        }
    }
}
