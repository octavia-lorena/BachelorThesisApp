package com.example.bachelorthesisapp.presentation.ui.navigation

import com.example.bachelorthesisapp.R


sealed class NavigationItemBusiness(var route: String, var icon: Int, var title: String) {
    object Feed: NavigationItemBusiness("posts_business/{uid}", R.drawable.baseline_feed_24, "Feed")
    object Home: NavigationItemBusiness("home_business/{uid}", R.drawable.baseline_home_24, "Home")
    object Requests: NavigationItemBusiness("requests_business/{uid}", R.drawable.baseline_notifications_24, "Requests")
}