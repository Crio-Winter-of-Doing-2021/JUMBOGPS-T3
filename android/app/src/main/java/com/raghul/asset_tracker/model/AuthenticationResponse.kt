package com.raghul.asset_tracker.model

data class AuthenticationResponse(
        val jsonToken: String,
        val expiresIn: Long
)