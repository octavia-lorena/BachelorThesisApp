package com.example.bachelorthesisapp.presentation.viewmodel.model

data class ActivityModel(
    val name: String,
    val type: String,
    val participants: Int,
    val price: Double,
    val link: String,
    val key: String,
    val accessibility: Double
)
