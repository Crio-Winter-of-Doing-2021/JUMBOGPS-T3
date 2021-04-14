package com.raghul.asset_tracker.config

import com.raghul.asset_tracker.model.AuthenticationRequest
import com.raghul.asset_tracker.service.UserService
import com.raghul.asset_tracker.utils.CommonConstants
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator: Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        println("inside authenticator")
        if(response.request().url().toString().contains("authenticate")){
            return response.request().newBuilder().build()
        }
        var token = authenticateToken(SharedPreferenceUtils.getString(CommonConstants.USERNAME)!!,SharedPreferenceUtils.getString(CommonConstants.PASSWORD)!!)
        return token?.let {
            response.request().newBuilder().header("Authorization", "Bearer $it").build()
        }
    }

    private fun authenticateToken(username: String,password: String): String?{
        var requestObj = AuthenticationRequest(username,password)
        var responseObj = RetrofitServiceBuilder.getService(UserService::class.java).authenticate(requestObj).execute().body()
        SharedPreferenceUtils.putString(CommonConstants.TOKEN,"${responseObj?.jsonToken}")
        return SharedPreferenceUtils.getString(CommonConstants.TOKEN)
    }

}