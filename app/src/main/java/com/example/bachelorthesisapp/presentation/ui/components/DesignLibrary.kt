package com.example.bachelorthesisapp.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.bachelorthesisapp.presentation.ui.theme.EventPlannerProjectTheme

@Preview(showBackground = true)
@Composable
fun AppBarPreview() {
    EventPlannerProjectTheme {
        AppBar("Dummy title")
    }
}