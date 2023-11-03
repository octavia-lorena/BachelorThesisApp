package com.example.bachelorthesisapp.presentation.ui.screen.client

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.core.presentation.UiState
import com.example.bachelorthesisapp.presentation.ui.components.common.BusinessSecondaryAppBar
import com.example.bachelorthesisapp.presentation.ui.components.common.SettingsScreenContent
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel

@Composable
fun ClientProfileScreen(
    clientId: String,
    clientViewModel: ClientViewModel,
    navHostController: NavHostController
) {
    val client = clientViewModel.clientState.collectAsStateWithLifecycle(UiState.Loading)

    LaunchedEffect(key1 = null) {
        clientViewModel.findClientById(clientId)
    }

    Scaffold(
        topBar = {
            BusinessSecondaryAppBar(
                title = "My Profile",
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

        }

    }
}