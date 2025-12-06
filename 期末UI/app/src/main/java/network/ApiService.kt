package com.example.ui.network

import com.example.ui.model.LoginResponse
import com.example.ui.model.RegisterResponse
import com.example.ui.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    // 請求體類別
    data class LoginRequest(
        val email: String,
        val password: String
    )

    data class RegisterRequest(
        val name: String,
        val email: String,
        val password: String,
        val phone: String = ""
    )
}