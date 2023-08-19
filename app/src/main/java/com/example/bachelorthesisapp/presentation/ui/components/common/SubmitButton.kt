package com.example.bachelorthesisapp.presentation.ui.components.common

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.Typography

@Composable
fun SubmitButton(
    onClick: () -> Unit,
    text: String,
    backgroundColor: Color = CoralAccent,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .height(50.dp)
                .width(150.dp)
                .clip(RoundedCornerShape(50.dp))
                .shimmerEffect()
        )
    } else
        Button(
            modifier = Modifier
                .height(50.dp)
                .width(150.dp),
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
            elevation = ButtonDefaults.elevation(defaultElevation = 5.dp),
            shape = RoundedCornerShape(50.dp),
            enabled = enabled
        ) {
            Text(
                text = text,
                style = Typography.button,
                color = Color.White
            )
        }
}

@Composable
fun SmallSubmitButton(
    onClick: () -> Unit,
    text: String,
    backgroundColor: Color = Color.Gray,
    textColor: Color = Color.White,
    enabled: Boolean = true
) = Button(
    modifier = Modifier
        .wrapContentHeight()
        .wrapContentWidth(),
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
    elevation = ButtonDefaults.elevation(defaultElevation = 5.dp),
    shape = RoundedCornerShape(10.dp),
    enabled = enabled
) {
    Text(
        text = text,
        color = textColor,
        style = Typography.caption
    )
}
