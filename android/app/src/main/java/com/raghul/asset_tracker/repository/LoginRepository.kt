package com.raghul.asset_tracker.repository

import android.content.Context
import android.content.Intent
import com.raghul.asset_tracker.MainActivity
import com.raghul.asset_tracker.config.RetrofitServiceBuilder
import com.raghul.asset_tracker.config.SharedPreferenceUtils
import com.raghul.asset_tracker.model.AuthenticationRequest
import com.raghul.asset_tracker.model.AuthenticationResponse
import com.raghul.asset_tracker.service.UserService
import com.raghul.asset_tracker.utils.CommonConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository(private val context: Context) {

    fun authenticateUser(username:String,password: String,uiWork:(authRes:AuthenticationResponse)->Unit){
        RetrofitServiceBuilder.getService(UserService::class.java).authenticate(
                AuthenticationRequest(username,password)).enqueue(object: Callback<AuthenticationResponse> {
            override fun onResponse(
                    call: Call<AuthenticationResponse>,
                    response: Response<AuthenticationResponse>
            ) {

                if(response.isSuccessful && response.code() == 200){
                    uiWork(response.body()!!)
                }
            }

            override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
            }

        })




    }
}