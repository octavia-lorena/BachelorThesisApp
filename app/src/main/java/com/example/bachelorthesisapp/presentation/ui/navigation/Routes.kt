package com.example.bachelorthesisapp.presentation.ui.navigation

const val ArgExampleId = "example_id"

sealed class Routes(val route: String) {
    object MainScreen : Routes("main_screen")
    object LoginScreen : Routes("login_screen")
    object MainRegisterScreen : Routes("main_register_screen")
    object ClientRegisterScreen : Routes("client_register_screen")
    object BusinessRegisterStep1Screen : Routes("business_register_step1_screen")
    object BusinessRegisterStep2Screen : Routes("business_register_step2_screen")
    object CreateOfferPostScreen : Routes("create_post")

    object CreateEventStep1Screen : Routes("create_event_step1")
    object CreateEventStep2Screen : Routes("create_event_step2")


    object ScreenWithArgument : Routes("argscreen/{$ArgExampleId}") {
        fun createRouteInt(exampleId: Int) = "argscreen/$exampleId"
        fun createRouteString(exampleId: Int) = "argscreen/$exampleId"
    }

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}