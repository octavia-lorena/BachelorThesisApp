package com.example.bachelorthesisapp.presentation.ui.components.common

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import com.example.bachelorthesisapp.presentation.ui.navigation.Routes
import com.example.bachelorthesisapp.presentation.ui.theme.Typography

@Composable
fun ErrorText(text: String){
    Text(
        color = Color.Red,
        text = text,
        style = Typography.subtitle1
    )
}

@Composable
fun BottomClickableText(text: String, onClick: (Int) -> Unit, color: Color){
    ClickableText(
        text = AnnotatedString(
            text = text
        ),
        onClick = onClick,
        style = Typography.subtitle1.copy(color = color),
        )
}