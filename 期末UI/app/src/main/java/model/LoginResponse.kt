package com.example.ui.model

data class LoginResponse(
    val success: Boolean = false,
    val message: String = "",
    val token: String = "",
    val user: User = User()
)