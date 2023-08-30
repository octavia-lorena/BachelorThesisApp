package com.example.bachelorthesisapp.data.model.validators

class LoginFormValidator {
    fun validateEmail(email: String): ValidationResult {
        if (email.isEmpty())
            return ValidationResult(false, "This field cannot be blank.")
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return ValidationResult(false, "Invalid email format.")
        return ValidationResult(success = true)
    }

    fun validatePassword(password: String): ValidationResult {
        if (password.isEmpty())
            return ValidationResult(false, "This field cannot be blank.")
        return ValidationResult(success = true)
    }
}