package com.raghul.asset_tracker.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.raghul.asset_tracker.R

class NotificationUtils(private val channelId:String,private  val notificationId:Int,private val context: Context) {


    /**
     * Method to create notification
     * @param title {String}
     * @param message {String}
     *
     */
    fun createNotification(title:String, message:String){
        var notificationChannel = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel(channelId,"Notification",NotificationManager.IMPORTANCE_DEFAULT)
        } else {
            null
        }
        var builder = NotificationCompat.Builder(context,channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.asset_truck)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
         val notificationManager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationChannel != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
        NotificationManagerCompat.from(context).notify(notificationId,builder.build())
    }


}