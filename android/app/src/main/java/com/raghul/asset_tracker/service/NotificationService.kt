package com.raghul.asset_tracker.service

import com.raghul.asset_tracker.model.Notification
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NotificationService {

    @GET("notification")
    fun getAllNotifications(): Call<List<Notification>>

    @POST("notification")
    fun saveNotification(@Body notification: Notification) : Call<ResponseBody>
}