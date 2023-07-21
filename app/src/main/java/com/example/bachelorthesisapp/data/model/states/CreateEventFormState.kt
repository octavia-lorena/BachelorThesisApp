package com.example.bachelorthesisapp.data.model.states

data class CreateEventFormState(
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val date: String = "",
    val dateError: String? = null,
    val time: String = "",
    val timeError: String? = null,
    val type: String = "",
    val typeError: String? = null,
    val guestNumber: String = "",
    val guestNumberError: String? = null,
    val budget: String = "",
    val budgetError: String? = null,
    val vendors: String = "",
    val vendorsError: String? = null
)