package com.example.bachelorthesisapp.presentation.ui.screen.business

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessSecondaryAppBar
import com.example.bachelorthesisapp.presentation.ui.components.common.SettingsScreenContent

@Composable
fun BusinessSettingsScreen(
    navHostController: NavHostController,
    darkThemeChecked: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            BusinessSecondaryAppBar(
                title = stringResource(R.string.settings),
                navController = navHostController
            )
        },
        backgroundColor = MaterialTheme.colors.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding(), top = 10.dp)
        ) {
            SettingsScreenContent(darkThemeChecked, onThemeChanged)
        }

    }
}