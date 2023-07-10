package com.example.bachelorthesisapp.data.model.events

sealed class ClientRegisterEvent {
    data class FirstNameChanged(val firstName: String) : ClientRegisterEvent()
    data class LastNameChanged(val lastName: String) : ClientRegisterEvent()
    data class EmailChanged(val email: String) : ClientRegisterEvent()
    data class PasswordChanged(val password: String) : ClientRegisterEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : ClientRegisterEvent()
    data class PhoneNumberChanged(val phoneNumber: String) : ClientRegisterEvent()
    object Submit: ClientRegisterEvent()
}