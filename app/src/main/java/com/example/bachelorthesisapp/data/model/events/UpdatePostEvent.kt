package com.example.bachelorthesisapp.data.model.events

sealed class UpdatePostEvent {
    data class TitleChanged(val title: String) : UpdatePostEvent()
    data class DescriptionChanged(val description: String) : UpdatePostEvent()
    data class ImagesChanged(val images: String) : UpdatePostEvent()
    data class PriceChanged(val price: String) : UpdatePostEvent()
    object Submit: UpdatePostEvent()
}