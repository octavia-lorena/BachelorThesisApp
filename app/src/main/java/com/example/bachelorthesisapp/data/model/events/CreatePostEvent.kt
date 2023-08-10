package com.example.bachelorthesisapp.data.model.events

sealed class CreatePostEvent {
    data class TitleChanged(val title: String) : CreatePostEvent()
    data class DescriptionChanged(val description: String) : CreatePostEvent()
    data class ImagesChanged(val images: String) : CreatePostEvent()
    data class PriceChanged(val price: String) : CreatePostEvent()
    object Submit: CreatePostEvent()
}