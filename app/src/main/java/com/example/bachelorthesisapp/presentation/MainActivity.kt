package com.example.bachelorthesisapp.presentation

import BusinessPostsHomeScreen
import EventsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bachelorthesisapp.presentation.ui.components.TransparentStatusBarHandler
import com.example.bachelorthesisapp.presentation.viewmodel.MainViewModel
import com.example.bachelorthesisapp.presentation.ui.navigation.Routes
import com.example.bachelorthesisapp.presentation.ui.screen.authentication.BusinessRegisterStep1Screen
import com.example.bachelorthesisapp.presentation.ui.screen.authentication.BusinessRegisterStep2Screen
import com.example.bachelorthesisapp.presentation.ui.screen.authentication.ClientRegisterScreen
import com.example.bachelorthesisapp.presentation.ui.screen.authentication.LoginScreen
import com.example.bachelorthesisapp.presentation.ui.screen.authentication.MainRegisterScreen
import com.example.bachelorthesisapp.presentation.ui.screen.business.BusinessHomeScreen
import com.example.bachelorthesisapp.presentation.ui.screen.business.CreateOfferPostScreen
import com.example.bachelorthesisapp.presentation.ui.screen.business.UpdateOfferPostScreen
import com.example.bachelorthesisapp.presentation.ui.screen.client.ClientHomeScreen
import com.example.bachelorthesisapp.presentation.ui.screen.client.CreateEventsStep1Screen
import com.example.bachelorthesisapp.presentation.ui.screen.client.CreateEventsStep2Screen
import com.example.bachelorthesisapp.presentation.ui.theme.EventPlannerProjectTheme
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.BusinessViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.CardSwipeViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
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
        TransparentStatusBarHandler.initTransparentStatusBar(window)

    }

}

@Composable
fun EventPlannerProjectDirection() {
    val authViewModel = hiltViewModel<AuthViewModel>()
    val navController: NavHostController = rememberNavController()
    hiltViewModel<MainViewModel>()
    val businessViewModel = hiltViewModel<BusinessViewModel>()
    val clientViewModel = hiltViewModel<ClientViewModel>()
    val cardsViewModel = hiltViewModel<CardSwipeViewModel>()

//    val isUserLoggedIn = authViewModel.currentUser != null
//    val currentUserId = authViewModel.currentUser?.uid
//    var startDestination = ""
//    startDestination = if (isUserLoggedIn)
//        "home_client/${currentUserId}"
//    else Routes.LoginScreen.route

    NavHost(navController = navController, startDestination = Routes.LoginScreen.route) {

        composable(route = Routes.LoginScreen.route) {
            LoginScreen(authViewModel = authViewModel, navHostController = navController)
        }
        composable(route = Routes.MainRegisterScreen.route) {
            MainRegisterScreen(navController = navController)
        }
        composable(
            route = Routes.ClientRegisterScreen.route
        ) {
            ClientRegisterScreen(authVM = authViewModel, navController = navController)
        }
        composable(
            route = Routes.BusinessRegisterStep1Screen.route
        ) {
            BusinessRegisterStep1Screen(authVM = authViewModel, navController = navController)
        }
        composable(
            route = Routes.BusinessRegisterStep2Screen.route
        ) {
            BusinessRegisterStep2Screen(authVM = authViewModel, navController = navController)
        }
        composable(
            route = "home_client/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })
        ) { backStackEntry ->
            ClientHomeScreen(
                uid = backStackEntry.arguments?.getString("uid")!!,
                authViewModel = authViewModel,
                clientViewModel = clientViewModel,
                navHostController = navController
            )
        }
        composable(
            route = "home_business/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })
        ) { backStackEntry ->
            BusinessHomeScreen(
                uid = backStackEntry.arguments?.getString("uid")!!,
                authViewModel = authViewModel,
                navHostController = navController
            )
        }
        composable(
            route = "posts_business/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })
        ) { backStackEntry ->
            BusinessPostsHomeScreen(
                uid = backStackEntry.arguments?.getString("uid")!!,
                authViewModel = authViewModel,
                navHostController = navController,
                onPostClick = {},
                businessViewModel = businessViewModel,
                cardsViewModel = cardsViewModel
            )
        }
        composable(
            route = Routes.CreateOfferPostScreen.route,
        ) {
            CreateOfferPostScreen(
                navController = navController,
                businessViewModel = businessViewModel
            )
        }
        composable(
            route = "update_post/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.IntType })
        ) { backStackEntry ->
            UpdateOfferPostScreen(
                postId = backStackEntry.arguments?.getInt("uid")!!,
                navController = navController,
                businessViewModel = businessViewModel
            )
        }
        composable(
            route = "events/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })
        ) { backStackEntry ->
            EventsScreen(
                uid = backStackEntry.arguments?.getString("uid")!!,
                authViewModel = authViewModel,
                navHostController = navController,
                onPostClick = {},
                clientViewModel = clientViewModel,
                cardsViewModel = cardsViewModel
            )
        }
        composable(
            route = Routes.CreateEventStep1Screen.route,
        ) {
            CreateEventsStep1Screen(
                navHostController = navController,
                clientViewModel = clientViewModel
            )
        }
        composable(
            route = Routes.CreateEventStep2Screen.route,
        ) {
            CreateEventsStep2Screen(
                navHostController = navController,
                clientViewModel = clientViewModel
            )
        }
    }
}