package com.raghul.asset_tracker.service

import com.raghul.asset_tracker.model.Asset
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface AssetService {

    @GET("assets")
    fun getAssets(): Call<MutableList<Asset>>

    @DELETE("assets/delete/{assetId}")
    fun deleteAsset(@Path("assetId") assetId: String): Call<Unit>

}