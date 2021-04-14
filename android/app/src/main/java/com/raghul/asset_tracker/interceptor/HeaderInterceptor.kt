package com.raghul.asset_tracker.interceptor

import com.raghul.asset_tracker.config.RetrofitServiceBuilder
import com.raghul.asset_tracker.config.SharedPreferenceUtils
import com.raghul.asset_tracker.model.AuthenticationRequest
import com.raghul.asset_tracker.service.UserService
import com.raghul.asset_tracker.utils.CommonConstants
import okhttp3.*

class HeaderInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
       val request : Request = chain.request()
        val token = SharedPreferenceUtils.getString(CommonConstants.TOKEN)
        println(token)
        val builder: Request.Builder = request.newBuilder()
        if(request.url().toString().contains("authenticate")){
            println("raghul over")
            val modifiedRequest : Request = builder.method(request.method(),request.body()).build()
            return chain.proceed(modifiedRequest)

        }
        val modifiedRequest : Request = builder.method(request.method(),request.body()).header("Authorization",
        "Bearer $token").build()

        return chain.proceed(modifiedRequest)
    }



}