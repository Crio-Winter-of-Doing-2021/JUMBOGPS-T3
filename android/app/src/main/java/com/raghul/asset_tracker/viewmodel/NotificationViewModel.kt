package com.raghul.asset_tracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.raghul.asset_tracker.model.Notification
import com.raghul.asset_tracker.repository.NotificationRepository

class NotificationViewModel(application: Application):AndroidViewModel(application) {
    private val notificationRepository:NotificationRepository = NotificationRepository(context = application.applicationContext)
    private val notifications = notificationRepository.getAllNotifications()


    fun getAllNotifications():MutableLiveData<List<Notification>>{
        return notifications;
    }
}