package com.example.bachelorthesisapp.presentation.ui.navigation

const val ArgExampleId = "example_id"

sealed class Routes(val route: String) {
    object MainScreen : Routes("main_screen")

    object ScreenWithArgument : Routes("argscreen/{$ArgExampleId}") {
        fun createRoute(exampleId: Int) = "argscreen/$exampleId"
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