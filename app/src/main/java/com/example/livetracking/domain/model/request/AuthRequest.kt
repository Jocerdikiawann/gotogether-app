package com.example.livetracking.domain.model.request

data class AuthRequest(
    val googleId: String,
    val email: String,
    val name: String
)