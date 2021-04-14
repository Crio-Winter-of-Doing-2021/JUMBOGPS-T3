package com.raghul.asset_tracker.service

import com.raghul.asset_tracker.model.AuthenticationRequest
import com.raghul.asset_tracker.model.AuthenticationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("user/authenticate")
    fun authenticate(@Body request:AuthenticationRequest): Call<AuthenticationResponse>
}