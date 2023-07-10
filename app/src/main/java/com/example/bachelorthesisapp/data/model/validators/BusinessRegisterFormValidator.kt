package com.example.bachelorthesisapp.data.model.validators

class BusinessRegisterFormValidator {

    fun validateName(name: String): ValidationResult {
        if (name.isEmpty())
            return ValidationResult(false, "This field cannot be blank.")
        return ValidationResult(success = true)
    }

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

    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationResult {
        if (confirmPassword.isEmpty())
            return ValidationResult(false, "This field cannot be blank.")
        if (confirmPassword != password)
            return ValidationResult(false, "Passwords do not match!")
        return ValidationResult(success = true)
    }

    fun validatePhoneNumber(number: String): ValidationResult {
        if (number.isEmpty())
            return ValidationResult(false, "This field cannot be blank.")
        return ValidationResult(success = true)
    }

    fun validateType(type: String): ValidationResult {
        if (type.isEmpty())
            return ValidationResult(false, "This field cannot be blank.")
        return ValidationResult(success = true)
    }

    fun validateAddress(address: String): ValidationResult{
        if (address.isEmpty())
            return ValidationResult(false, "This field cannot be blank.")
//        val addressRegex = """.+;.+;.+""".toRegex()
//        if (!addressRegex.matches(address))
//            return ValidationResult(false, "Wrong address format")
        return ValidationResult(success = true)
    }
}