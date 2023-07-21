package com.example.bachelorthesisapp.presentation.ui.navigation

import com.example.bachelorthesisapp.R


sealed class NavigationItemBusiness(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItemBusiness(
        route = "home_business/{uid}",
        icon = R.drawable.baseline_home_24,
        title = "Home"
    )

    object Feed :
        NavigationItemBusiness(
            route = "posts_business/{uid}",
            icon = R.drawable.baseline_feed_24,
            title = "Feed"
        )

    object Requests : NavigationItemBusiness(
        route = "requests_business/{uid}",
        icon = R.drawable.baseline_notifications_24,
        title = "Requests"
    )
}