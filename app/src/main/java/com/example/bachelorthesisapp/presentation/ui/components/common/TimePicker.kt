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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalTime

@Preview
@Composable
fun DropdownTimeMenu(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String = "Time",
    onItemSelected: (item: String) -> Unit = {},
    busyDates: List<String> = emptyList(),
    initialTime: String = "Pick a time"
) {
    var expanded by remember { mutableStateOf(false) }
    var pickedTime by remember {
        mutableStateOf(LocalTime.now())
    }
    val dateDialogState = rememberMaterialDialogState()
    var dateValue by remember {
        mutableStateOf(initialTime)
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
                    painter = painterResource(id = R.drawable.baseline_access_time_24),
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
                positiveButton(text = "Ok", textStyle = TextStyle(color = Color.White)) {
                    val selectedTime = pickedTime.toString().split(":")
                    val timeString = selectedTime[0] + ":" + selectedTime[1]
                    onItemSelected(timeString)
                    dateValue = timeString
                    expanded = false
                }
                negativeButton(text = "Cancel", textStyle = TextStyle(color = Color.White)) {
                   // onItemSelected(pickedTime.toString())
                    expanded = false
                }
            },
            autoDismiss = true
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
            ) {
                timepicker(
                    initialTime = LocalTime.now(),
                    title = "Pick the time",
                    colors = TimePickerDefaults.colors(
                        headerTextColor = Color.Gray,
                        activeBackgroundColor = CoralLight,
                        selectorColor = CoralAccent,
                        selectorTextColor = Color.White,
                    ),
                    waitForPositiveButton = true,
                    is24HourClock = true,
                    onTimeChange = {
                        pickedTime = it
                    }
                )
            }

        }
    }
}
