package com.raghul.asset_tracker.service

import com.raghul.asset_tracker.model.RefTerm
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ReferenceService {

    @GET("refterm")
    fun getRefTerms(@Query("key") key:String): Call<List<RefTerm>>
}