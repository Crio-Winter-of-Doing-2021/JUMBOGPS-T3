package com.raghul.asset_tracker.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.raghul.asset_tracker.config.RetrofitServiceBuilder
import com.raghul.asset_tracker.model.Notification
import com.raghul.asset_tracker.service.NotificationService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NotificationRepository(private val context: Context) {

    fun getAllNotifications():MutableLiveData<List<Notification>>{
        val data:MutableLiveData<List<Notification>> = MutableLiveData()
        RetrofitServiceBuilder.getService(NotificationService::class.java).getAllNotifications().enqueue(object : Callback<List<Notification>> {
            override fun onResponse(call: Call<List<Notification>>, response: Response<List<Notification>>) {
             if(response.isSuccessful){
                 println(response.body())
                 data.postValue(response.body())
             }
            }

            override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
                data.postValue(Collections.emptyList())
            }

        })

        return data
    }

    fun saveNotification(notification: Notification){
        RetrofitServiceBuilder.getService(NotificationService::class.java).saveNotification(notification)
            .enqueue(object:Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if(response.isSuccessful){
                        println("success")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("failure")
                }

            })
    }

}