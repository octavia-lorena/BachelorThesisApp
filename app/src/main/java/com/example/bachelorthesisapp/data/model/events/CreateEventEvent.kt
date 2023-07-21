package com.example.bachelorthesisapp.data.model.events

sealed class CreateEventEvent {
    data class TitleChanged(val title: String) : CreateEventEvent()
    data class DescriptionChanged(val description: String) : CreateEventEvent()
    data class TypeChanged(val type: String) : CreateEventEvent()
    data class DateChanged(val date: String) : CreateEventEvent()
    data class TimeChanged(val time: String) : CreateEventEvent()
    data class GuestNumberChanged(val guestNumber: String) : CreateEventEvent()
    data class BudgetChanged(val budget: String) : CreateEventEvent()
    data class VendorsChanged(val vendors: String): CreateEventEvent()
    object Submit : CreateEventEvent()
}