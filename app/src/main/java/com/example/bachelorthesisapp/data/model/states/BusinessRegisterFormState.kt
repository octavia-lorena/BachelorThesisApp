package com.example.bachelorthesisapp.data.model.states

data class BusinessRegisterFormState(
    val name: String = "",
    val nameError: String? = null,
    val type: String = "",
    val typeError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val city: String = "",
    val cityError: String? = null,
    val address: String = "",
    val addressError: String? = null,
    val email: String= "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val lat: String = "",
    val lng: String = "",
    val profilePicture: String = ""
)