package com.raghul.asset_tracker.service

import com.raghul.asset_tracker.model.Asset
import com.raghul.asset_tracker.model.AssetGeofence
import com.raghul.asset_tracker.model.AssetRoute
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.POST


interface LocationService {


    @GET("gps/assets")
    fun getAssetsLocation(@Query("assetId") assetId: String?,
                          @Query("assetTypes") assetTypes: MutableList<String>?,
    @Query("startTime") startDateTime: String?,
    @Query("endTime") endTime: String?,
    @Query("size") size: Int?) : Call<List<Asset>>

    @GET("gps/{assetId}/timeline")
    fun getAssetTimeline(@Path("assetId") assetId: String) : Call<Asset>

    @GET("gps/assets/geofence")
    fun getAssetsGeofence(@Query("assetIds") assetIds:MutableList<String>) : Call<List<AssetGeofence>>

    @POST("gps/{assetId}/geofence")
    fun saveAssetGeofence(@Path("assetId") assetId: String,@Body geofence: AssetGeofence): Call<ResponseBody>

    @POST("gps/{assetId}/asset-route")
    fun saveAssetRoute(@Path("assetId") assetId: String,@Body assetRoute: AssetRoute): Call<ResponseBody>

    @GET("gps/{assetId}/asset-route")
    fun getAssetRoute(@Path("assetId") assetId: String): Call<AssetRoute>
}