package com.example.sonuslink

object Validator {

    /**
     * Valida que el email no esté vacío y cumpla con el formato estándar de correo electrónico.
     */
    fun isEmailValid(email: String): Boolean {
        if (email.isBlank()) return false
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(emailRegex)
    }

    /**
     * Valida que la contraseña cumpla con el mínimo de 6 caracteres requerido por Firebase Auth.
     */
    fun isPasswordSecure(password: String): Boolean {
        return password.trim().length >= 6
    }
}