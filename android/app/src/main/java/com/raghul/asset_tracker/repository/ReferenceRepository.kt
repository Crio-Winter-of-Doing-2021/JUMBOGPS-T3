package com.raghul.asset_tracker.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.raghul.asset_tracker.config.RetrofitServiceBuilder
import com.raghul.asset_tracker.model.RefTerm
import com.raghul.asset_tracker.service.ReferenceService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReferenceRepository (private val context: Context) {

    fun getReferenceValues(key:String):MutableLiveData<List<RefTerm>>{
        var values:MutableLiveData<List<RefTerm>>  = MutableLiveData()
        RetrofitServiceBuilder.getService(ReferenceService::class.java)
                .getRefTerms(key)
                .enqueue(object: Callback<List<RefTerm>>{
                    override fun onResponse(call: Call<List<RefTerm>>, response: Response<List<RefTerm>>) {
                        if(response.isSuccessful){
                            values.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<List<RefTerm>>, t: Throwable) {
                    }

                })

        return values
    }

}