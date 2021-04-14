package com.raghul.asset_tracker.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raghul.asset_tracker.R
import com.raghul.asset_tracker.model.Asset

class AssetListAdapter(private var timelineClickListener: TimelineClickListener,private var deleteClickListener:DeleteItemListener) : RecyclerView.Adapter<AssetListAdapter.AssetViewHolder>() {


    companion object{
        var mClickListener:TimelineClickListener?=null
        var assetsList:MutableList<Asset>?=null
        var mDeleteListener:DeleteItemListener?=null
    }


    open interface TimelineClickListener{
         fun onTimelineClick(asset: Asset)
    }

    open interface DeleteItemListener{
        fun onDeleteClick(asset: Asset)
    }

    public fun setAssetList(assets:MutableList<Asset> ){
        assetsList = assets
        notifyDataSetChanged()
    }

    class AssetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {
        private val assetName: TextView = itemView.findViewById(R.id.asset_name)
        private val assetType: TextView = itemView.findViewById(R.id.asset_type)
        private val assetImage:ImageView = itemView.findViewById(R.id.asset_image)
        private val assetTimeline: Button = itemView.findViewById(R.id.timeline_button)
        private val assetDelete: ImageView = itemView.findViewById(R.id.asset_item_delete)
        fun bindItem(asset: Asset){
            assetName.text = asset.assetName
            assetType.text = asset.assetType
            assetDelete.setOnClickListener {
                assetsList?.get(adapterPosition)?.let {
                    mDeleteListener?.onDeleteClick(it)
                }
            }
            assetTimeline.setOnClickListener(this)
            when(asset.assetType){
                "truck" -> assetImage.setImageResource(R.drawable.asset_truck)
                "salesperson"->assetImage.setImageResource(R.drawable.asset_salesperson)
                else -> assetImage.setImageResource(R.drawable.asset_truck)

            }


        }

        override fun onClick(p0: View?) {
            assetsList?.get(adapterPosition)?.let { mClickListener?.onTimelineClick(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.asset_item, parent, false)
        return AssetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        mClickListener = timelineClickListener
        mDeleteListener = deleteClickListener
        assetsList?.get(position)?.let { holder.bindItem(it) }
    }

    override fun getItemCount(): Int {
        return assetsList?.size ?: 0
    }
}