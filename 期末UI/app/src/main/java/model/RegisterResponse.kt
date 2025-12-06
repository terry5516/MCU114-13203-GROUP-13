package com.example.ui.model

data class RegisterResponse(
    val success: Boolean = false,
    val message: String = "",
    val user: User = User()
)