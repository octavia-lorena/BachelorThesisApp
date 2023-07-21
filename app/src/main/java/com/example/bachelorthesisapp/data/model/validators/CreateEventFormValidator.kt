package com.example.bachelorthesisapp.data.model.validators

class CreateEventFormValidator {
    fun validateTitle(title: String): ValidationResult {
        if (title.isEmpty())
            return ValidationResult(false, "Title cannot be blank.")
        if (!title[0].isUpperCase())
            return ValidationResult(false, "Title must start with an uppercase letter.")
        return ValidationResult(success = true)
    }

    fun validateDescription(description: String): ValidationResult {
        if (description.isEmpty())
            return ValidationResult(false, "Description cannot be blank.")
        if (description.length < 20)
            return ValidationResult(false, "Please provide a more detailed description.")
        if (!description[0].isUpperCase())
            return ValidationResult(false, "Description must start with an uppercase letter.")
        return ValidationResult(success = true)
    }

    fun validatePhotos(photos: String): ValidationResult {
        if (photos.isEmpty())
            return ValidationResult(false, "Photos cannot be blank.")
        val photosStr = photos.split(";")
        if (photosStr.size < 3)
            return ValidationResult(false, "You must insert at least 3 photos.")
        return ValidationResult(success = true)
    }

    fun validatePrice(price: String): ValidationResult {
        if (price.isEmpty())
            return ValidationResult(false, "Price cannot be blank.")
        val priceValue = price.toIntOrNull()
        if (priceValue == null)
            return ValidationResult(false, "Invalid numeric format.")
        else {
            if (priceValue <= 0)
                return ValidationResult(false, "Price cannot be <= 0")
        }
        return ValidationResult(success = true)
    }
}