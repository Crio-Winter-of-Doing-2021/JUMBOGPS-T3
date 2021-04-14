package com.raghul.asset_tracker

import android.app.Application
import android.content.Context
import com.mapbox.mapboxsdk.Mapbox
import com.raghul.asset_tracker.config.SharedPreferenceUtils

class AssetTracker : Application() {
    companion object{
     lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        SharedPreferenceUtils.getInstance(context)
        Mapbox.getInstance(context,getString(R.string.map_box_token))
    }
}