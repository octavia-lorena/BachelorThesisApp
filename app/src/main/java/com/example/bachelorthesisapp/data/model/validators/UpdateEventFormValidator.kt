package com.example.bachelorthesisapp.data.model.validators

class UpdateEventFormValidator {
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

    fun validateDate(date: String): ValidationResult {
        if (date.isEmpty())
            return ValidationResult(false, "Date cannot be blank.")
        return ValidationResult(success = true)
    }

    fun validateTime(time: String): ValidationResult {
        if (time.isEmpty())
            return ValidationResult(false, "Time cannot be blank.")
        if (time.length != 5) {
            return ValidationResult(false, "Invalid time format.")
        }
        if (time[2] != ':') {
            return ValidationResult(false, "Invalid time format.")
        }
        val parts = time.split(':')
        if (parts[0].length != 2 && parts[1].length != 2) {
            return ValidationResult(false, "Invalid time format.")
        }
        if (parts[0].toIntOrNull() == null && parts[1].toIntOrNull() == null) {
            return ValidationResult(false, "Invalid time format.")
        }
        return ValidationResult(success = true)
    }

    fun validateGuestNumber(guestNr: String): ValidationResult {
        if (guestNr.isEmpty())
            return ValidationResult(false, "Guest number cannot be blank.")
        return ValidationResult(success = true)
    }

    fun validateBudget(budget: String): ValidationResult {
        if (budget.isEmpty())
            return ValidationResult(false, "Budget cannot be blank.")
        return ValidationResult(success = true)
    }

}