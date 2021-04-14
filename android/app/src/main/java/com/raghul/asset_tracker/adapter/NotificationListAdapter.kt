package com.raghul.asset_tracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raghul.asset_tracker.R
import com.raghul.asset_tracker.model.Notification

class NotificationListAdapter:
    RecyclerView.Adapter<NotificationListAdapter.NotificationViewHolder>() {

    companion object{
        var notificationList:List<Notification>?=null
    }

    fun setNotificationList(notificationList:List<Notification>){
        NotificationListAdapter.notificationList = notificationList
        notifyDataSetChanged()
    }

    class NotificationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val notificationMessage:TextView = itemView.findViewById(R.id.notification_message)
        private val notificationImageView:ImageView = itemView.findViewById(R.id.notification_image)

        fun bindData(notifObj:Notification){
            notificationMessage.text = notifObj.message
            var imageId = when (notifObj.asset?.assetType){
                "truck"->R.drawable.asset_truck
                else -> R.drawable.asset_salesperson
            }
            notificationImageView.setImageResource(imageId)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        var layoutView:View = LayoutInflater.from(parent.context).inflate(R.layout.notification_item,parent,false)
        return NotificationViewHolder(layoutView)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        notificationList?.get(position)?.let { holder.bindData(it) }
    }

    override fun getItemCount(): Int {
        return if(NotificationListAdapter.notificationList == null) 0 else NotificationListAdapter.notificationList?.size!!
    }
}