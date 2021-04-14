package com.raghul.asset_tracker.config

import com.raghul.asset_tracker.interceptor.HeaderInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServiceBuilder {
    private val okClient:OkHttpClient = OkHttpClient().newBuilder().addInterceptor(HeaderInterceptor())
            .authenticator(TokenAuthenticator())
        .build()
    private val retrofitBuilder = Retrofit.Builder().
    baseUrl("https://asset-tracker-backend.herokuapp.com/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okClient)
        .build()
    fun<T> getService(className: Class<T>):T{
        return retrofitBuilder.create(className)
    }
}