package com.example.bachelorthesisapp.presentation.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.data.model.events.ClientRegisterEvent
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.Ochre
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.ui.theme.Typography

@Composable
fun SubmitButton(onClick: () -> Unit, text: String) = Button(
    modifier = Modifier
        .height(50.dp)
        .width(150.dp),
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(backgroundColor = Coral),
    elevation = ButtonDefaults.elevation(defaultElevation = 5.dp),
    shape = RoundedCornerShape(50.dp)
) {
    Text(
        text = text,
        style = Typography.subtitle1,
        color = Color.White
    )
}

@Composable
fun SmallSubmitButton(onClick: () -> Unit, text: String) = Button(
    modifier = Modifier
        .wrapContentHeight()
        .wrapContentWidth(),
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
    elevation = ButtonDefaults.elevation(defaultElevation = 5.dp),
    shape = RoundedCornerShape(10.dp)
) {
    Text(text = text)
}
