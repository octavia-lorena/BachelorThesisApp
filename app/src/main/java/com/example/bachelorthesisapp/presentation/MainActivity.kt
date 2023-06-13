package com.example.bachelorthesisapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bachelorthesisapp.presentation.viewmodel.MainViewModel
import com.example.bachelorthesisapp.presentation.ui.navigation.Routes
import com.example.bachelorthesisapp.presentation.ui.screen.MainScreen
import com.example.bachelorthesisapp.presentation.ui.theme.EventPlannerProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EventPlannerProjectTheme {
                EventPlannerProjectDirection()
            }
        }
    }
}

@Composable
fun EventPlannerProjectDirection() {
    val navController: NavHostController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.MainScreen.route) {
        composable(route = Routes.MainScreen.route) {
            val mainViewModel = hiltViewModel<MainViewModel>()
            MainScreen(mainViewModel)
        }
    }
}