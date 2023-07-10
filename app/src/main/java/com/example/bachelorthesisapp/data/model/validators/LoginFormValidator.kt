package com.example.bachelorthesisapp.data.model.validators

class LoginFormValidator {
    fun validateEmail(email: String): ValidationResult {
        if (email.isEmpty())
            return ValidationResult(false, "This field cannot be blank.")
        return ValidationResult(success = true)
    }

    fun validatePassword(password: String): ValidationResult {
        if (password.isEmpty())
            return ValidationResult(false, "This field cannot be blank.")
        if (password.length < 7)
            return ValidationResult(false, "Password must be at least 7 characters long.")
        return ValidationResult(success = true)
    }
}