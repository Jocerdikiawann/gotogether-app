package com.example.livetracking.domain.model.response

data class AuthResponse(
    val id: String,
    val googleId: String,
    val email: String,
    val name: String
)