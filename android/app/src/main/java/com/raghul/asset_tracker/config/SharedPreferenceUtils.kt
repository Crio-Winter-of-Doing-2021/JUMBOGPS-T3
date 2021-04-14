package com.raghul.asset_tracker.config

import android.content.Context
import android.content.SharedPreferences
import com.raghul.asset_tracker.R

object SharedPreferenceUtils {


    private var context: Context? = null
    private var settings: SharedPreferences? = null

        fun getInstance(context: Context) {
            this.context = context
           settings =  context.getSharedPreferences(context.getString(R.string.app_name),Context.MODE_PRIVATE)
        }


        fun putString(key: String,value: String){
            var editor: SharedPreferences.Editor? = settings?.edit()
            editor?.putString(key,value)
            editor?.commit()
        }

    fun getString(key: String?,default: String?=""): String?{
        return settings?.getString(key,default)
    }

    fun clearAllPreference(){
        var editor : SharedPreferences.Editor? = settings?.edit()
        editor?.clear()
        editor?.commit()
    }



}