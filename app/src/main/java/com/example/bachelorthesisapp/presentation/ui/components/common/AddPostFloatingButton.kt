package com.example.bachelorthesisapp.presentation.ui.components.common

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.presentation.ui.navigation.Routes
import com.example.bachelorthesisapp.presentation.ui.theme.Rose

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
        containerColor = Rose,
        shape = RoundedCornerShape(50.dp)
    )
}

@Composable
fun AddPostExpandableFloatingButton(navHostController: NavHostController, expanded: Boolean) {
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
        containerColor = Rose,
        expanded = expanded,
        shape = RoundedCornerShape(50.dp)
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
        containerColor = Rose,
        shape = RoundedCornerShape(50.dp)
    )
}

@Composable
fun CreateEventExpandableFloatingButton(
    navHostController: NavHostController,
    uid: String,
    expanded: Boolean
) {
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
        containerColor = Rose,
        expanded = expanded,
        shape = RoundedCornerShape(50.dp)
    )
}