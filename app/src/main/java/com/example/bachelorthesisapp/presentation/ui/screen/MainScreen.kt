package com.example.bachelorthesisapp.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bachelorthesisapp.presentation.viewmodel.MainViewModel
import com.example.bachelorthesisapp.presentation.ui.components.AppBar
import com.example.bachelorthesisapp.presentation.viewmodel.model.ActivityModel
import com.example.bachelorthesisapp.presentation.viewmodel.state.UiState


@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state = viewModel.activityState.collectAsStateWithLifecycle(UiState.Loading)

    LaunchedEffect(key1 = true) {
        viewModel.loadActivityData()
    }
    MainScreenContent(state.value)

}

@Composable
fun MainScreenContent(content: UiState<ActivityModel> = UiState.Loading){
    Scaffold(topBar = {
        AppBar(title = "Main Screen")
    }, content = {
        it
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            when (content) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is UiState.Success -> {
                    val activityData = content.value
                    Text(text = activityData.name)
                    Text(text = activityData.type)
                    Text(text = activityData.participants.toString())
                    Text(text = activityData.price.toString())
                    Text(text = activityData.link)
                    Text(text = activityData.key)
                    Text(text = activityData.accessibility.toString())
                }

                is UiState.Error -> {
                    Text(text = content.cause.toString())
                }
            }
        }
    })
}

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreenContent(
        content = UiState.Success(
            ActivityModel(
                key = "foo",
                link = "foo",
                name = "foooo",
                participants = 2,
                price = 1.0,
                type = "foo",
                accessibility = 1.0,
            )
        )
    )
}
