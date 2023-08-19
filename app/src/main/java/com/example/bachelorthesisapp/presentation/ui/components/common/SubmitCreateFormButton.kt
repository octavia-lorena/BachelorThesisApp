package com.example.bachelorthesisapp.presentation.ui.components.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.Typography

@Composable
fun SubmitCreateFormButton(
    onButtonClick: () -> Unit,
    text: String,
    isLoading: Boolean = false
) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .height(50.dp)
                .width(150.dp)
                .shimmerEffect()
        )
    }
    Button(
        onClick = onButtonClick,
        modifier = Modifier.wrapContentSize(),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        border = BorderStroke(
            width = 1.dp, brush = Brush.horizontalGradient(
                listOf(
                    CoralAccent,
                    Coral,
                    CoralAccent
                )
            )
        )
    ) {
        Text(
            text = text,
            style = Typography.caption,
            color = Color.DarkGray
        )
    }
}