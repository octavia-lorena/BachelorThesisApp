package com.example.bachelorthesisapp.presentation.ui.navigation

import com.example.bachelorthesisapp.R


sealed class NavigationItemClient(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItemClient(
        route = "home_client/{uid}",
        icon = R.drawable.baseline_home_24,
        title = "Home"
    )

    object Feed :
        NavigationItemClient(
            route = "events/{uid}",
            icon = R.drawable.baseline_feed_24,
            title = "Events"
        )

}