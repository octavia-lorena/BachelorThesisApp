package com.example.bachelorthesisapp.presentation

import com.example.bachelorthesisapp.presentation.ui.screen.business.BusinessPostsHomeScreen
import EventsScreen
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bachelorthesisapp.data.repo.firebase.FirebaseMessageService
import com.example.bachelorthesisapp.presentation.ui.components.common.TransparentStatusBarHandler
import com.example.bachelorthesisapp.presentation.ui.navigation.Routes
import com.example.bachelorthesisapp.presentation.ui.screen.authentication.BusinessRegisterStep1Screen
import com.example.bachelorthesisapp.presentation.ui.screen.authentication.BusinessRegisterStep2Screen
import com.example.bachelorthesisapp.presentation.ui.screen.authentication.ClientRegisterScreen
import com.example.bachelorthesisapp.presentation.ui.screen.authentication.LoginScreen
import com.example.bachelorthesisapp.presentation.ui.screen.authentication.MainRegisterScreen
import com.example.bachelorthesisapp.presentation.ui.screen.business.AppointmentsScreen
import com.example.bachelorthesisapp.presentation.ui.screen.business.BusinessHomeScreen
import com.example.bachelorthesisapp.presentation.ui.screen.business.CreateOfferPostScreen
import com.example.bachelorthesisapp.presentation.ui.screen.business.RequestsScreen
import com.example.bachelorthesisapp.presentation.ui.screen.business.UpdateOfferPostScreen
import com.example.bachelorthesisapp.presentation.ui.screen.client.BusinessProfileScreen
import com.example.bachelorthesisapp.presentation.ui.screen.client.ClientHomeScreen
import com.example.bachelorthesisapp.presentation.ui.screen.client.CreateEventsStep1Screen
import com.example.bachelorthesisapp.presentation.ui.screen.client.CreateEventsStep2Screen
import com.example.bachelorthesisapp.presentation.ui.screen.client.EventDetailsScreen
import com.example.bachelorthesisapp.presentation.ui.screen.client.UpdateEventScreen
import com.example.bachelorthesisapp.presentation.ui.theme.EventPlannerProjectTheme
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.BusinessViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.CardSwipeViewModel
import com.example.bachelorthesisapp.presentation.viewmodel.ClientViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EventPlannerProjectTheme {
                EventPlannerProjectDirection(askNotificationPermissionCall = { askNotificationPermission() })
            }
        }
        TransparentStatusBarHandler.initTransparentStatusBar(window)
        FirebaseMessageService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

}

@Composable
fun EventPlannerProjectDirection(askNotificationPermissionCall: () -> Unit) {
    val authViewModel = hiltViewModel<AuthViewModel>()
    val navController: NavHostController = rememberNavController()
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
                navHostController = navController,
                askNotificationPermissionCall = askNotificationPermissionCall
            )
        }
        composable(
            route = "home_business/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })
        ) { backStackEntry ->
            BusinessHomeScreen(
                uid = backStackEntry.arguments?.getString("uid")!!,
                authViewModel = authViewModel,
                navHostController = navController,
                askNotificationPermissionCall = askNotificationPermissionCall,
                clientViewModel = clientViewModel
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
                clientViewModel = clientViewModel,
                navHostController = navController
            ) { eventId -> clientViewModel.deleteEvent(eventId) }
        }
        composable(
            route = "create_event_step1/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })
        ) { backStackEntry ->
            CreateEventsStep1Screen(
                uid = backStackEntry.arguments?.getString("uid")!!,
                navHostController = navController,
                clientViewModel = clientViewModel
            )
        }
        composable(
            route = "create_event_step2/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })
        ) { backStackEntry ->
            CreateEventsStep2Screen(
                uid = backStackEntry.arguments?.getString("uid")!!,
                navHostController = navController,
                clientViewModel = clientViewModel
            )
        }
        composable(
            route = "event_details/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) { backStackEntry ->
            EventDetailsScreen(
                eventId = backStackEntry.arguments?.getInt("eventId")!!,
                navHostController = navController,
                clientViewModel = clientViewModel
            )
        }
        composable(
            route = "business_profile/{uid}/{eventId}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType },
                navArgument("eventId") { type = NavType.IntType })
        ) { backStackEntry ->
            BusinessProfileScreen(
                businessId = backStackEntry.arguments?.getString("uid")!!,
                eventId = backStackEntry.arguments?.getInt("eventId")!!,
                navHostController = navController,
                clientViewModel = clientViewModel
            )
        }
        composable(
            route = "requests/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })
        ) { backStackEntry ->
            RequestsScreen(
                uid = backStackEntry.arguments?.getString("uid")!!,
                authViewModel = authViewModel,
                clientViewModel = clientViewModel,
                navHostController = navController
            )
        }
        composable(
            route = "appointments/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.StringType })
        ) { backStackEntry ->
            AppointmentsScreen(
                uid = backStackEntry.arguments?.getString("uid")!!,
                authViewModel = authViewModel,
                clientViewModel = clientViewModel,
                businessViewModel = businessViewModel,
                navHostController = navController
            )
        }
        composable(
            route = "update_event/{uid}",
            arguments = listOf(navArgument("uid") { type = NavType.IntType })
        ) { backStackEntry ->
            UpdateEventScreen(
                eventId = backStackEntry.arguments?.getInt("uid")!!,
                clientViewModel = clientViewModel,
                navHostController = navController
            )
        }

    }
}