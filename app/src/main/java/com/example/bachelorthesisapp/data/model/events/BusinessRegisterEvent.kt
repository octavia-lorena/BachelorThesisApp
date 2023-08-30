package com.example.bachelorthesisapp.data.model.events

sealed class BusinessRegisterEvent {
    data class NameChanged(val name: String): BusinessRegisterEvent()
    data class TypeChanged(val type: String): BusinessRegisterEvent()
    data class PhoneNumberChanged(val phoneNumber: String): BusinessRegisterEvent()
    data class AddressChanged(val address: String): BusinessRegisterEvent()
    data class EmailChanged(val email: String): BusinessRegisterEvent()
    data class PasswordChanged(val password: String): BusinessRegisterEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : BusinessRegisterEvent()
    data class CityChanged(val city: String): BusinessRegisterEvent()
    data class ProfilePictureChanged(val profilePicture: String): BusinessRegisterEvent()
    object Submit: BusinessRegisterEvent()
    object PartialSubmit: BusinessRegisterEvent()

}