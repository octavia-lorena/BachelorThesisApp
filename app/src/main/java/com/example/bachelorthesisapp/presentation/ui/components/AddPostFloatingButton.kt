package com.example.bachelorthesisapp.presentation.ui.components

import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.presentation.ui.navigation.Routes
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.viewmodel.BusinessViewModel
import okhttp3.Route

@Composable
fun AddPostFloatingButton(navHostController: NavHostController) {
    ExtendedFloatingActionButton(
        text = { Text(text = "NEW POST", color = Color.White) },
        onClick = {
            // Navigate to Create Post Screen
            navHostController.navigate(Routes.CreateOfferPostScreen.route)
        },
        icon = {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "",
                tint = Color.White
            )
        },
        backgroundColor = Rose
    )
}

@Composable
fun CreateEventFloatingButton(navHostController: NavHostController, uid: String) {
    ExtendedFloatingActionButton(
        text = { Text(text = "CREATE EVENT", color = Color.White) },
        onClick = {
            // Navigate to Create Event Screen
            navHostController.navigate("create_event_step1/$uid")
        },
        icon = {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "",
                tint = Color.White
            )
        },
        backgroundColor = Rose
    )
}