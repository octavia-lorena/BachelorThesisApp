package com.example.bachelorthesisapp.data.model.events

sealed class UpdateEventEvent {
    data class NameChanged(val name: String) : UpdateEventEvent()
    data class DescriptionChanged(val description: String) : UpdateEventEvent()
    data class DateChanged(val date: String) : UpdateEventEvent()
    data class TimeChanged(val time: String) : UpdateEventEvent()
    data class GuestNumberChanged(val guestNumber: String) : UpdateEventEvent()
    data class BudgetChanged(val budget: String) : UpdateEventEvent()
    object Submit: UpdateEventEvent()
}