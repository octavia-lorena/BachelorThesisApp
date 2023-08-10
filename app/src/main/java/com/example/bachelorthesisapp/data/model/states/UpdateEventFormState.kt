package com.example.bachelorthesisapp.data.model.states


data class UpdateEventFormState(
    var id: Int = -1,
    val name: String = "",
    val nameError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val date: String = "",
    val dateError: String? = null,
    val time: String = "",
    val timeError: String? = null,
    val guestNumber: String = "",
    val guestNumberError: String? = null,
    val budget: String = "",
    val budgetError: String? = null,
)