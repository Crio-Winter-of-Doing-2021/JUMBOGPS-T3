package com.raghul.asset_tracker.repository

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions
import com.raghul.asset_tracker.R
import com.raghul.asset_tracker.config.RetrofitServiceBuilder
import com.raghul.asset_tracker.model.Asset
import com.raghul.asset_tracker.model.AssetGeofence
import com.raghul.asset_tracker.model.AssetRoute
import com.raghul.asset_tracker.service.LocationService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationRepository(private val context: Context) {


    /**
     * Method to get the asset location
     * @param searchText {String}
     * @param assetTypes {MutableList<String>}
     * @param startDateTime {String}
     * @param endDateTime {String}
     * @param size {Int}
     * @return location of asset {List<Asset>}
     */
    fun getAssetsCurrentLocation(searchText: String?,assetTypes:MutableList<String>?,startDateTime:String?,
                                 endDateTime:String?,size:Int?): MutableLiveData<List<Asset>>{
        var assets : MutableLiveData<List<Asset>> = MutableLiveData<List<Asset>>()
        RetrofitServiceBuilder.getService(LocationService::class.java).getAssetsLocation(searchText,assetTypes,
            startDateTime,endDateTime,size).enqueue(object: Callback<List<Asset>> {
            override fun onResponse(call: Call<List<Asset>>, response: Response<List<Asset>>) {
                if(response.isSuccessful){
                    assets.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<Asset>>, t: Throwable) {
            }

        })
        return assets
    }

    /**
     * Method to get the asset timeline
     * @param assetId {String}
     * @return asset timeline {Asset}
     */
    fun getAssetCurrentTimeline(assetId: String): MutableLiveData<Asset>{
        var asset:MutableLiveData<Asset> = MutableLiveData<Asset>()
        RetrofitServiceBuilder.getService(LocationService::class.java).getAssetTimeline(assetId).enqueue(object:Callback<Asset>{
            override fun onResponse(call: Call<Asset>, response: Response<Asset>) {
                if(response.isSuccessful){
                    asset.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<Asset>, t: Throwable) {
            }

        })

        return asset

    }

    /**
     * Method to get asset route
     * @param assetId {String}
     * @return asset route {AssetRoute}
     */
    fun getAssetRoute(assetId: String):MutableLiveData<AssetRoute>{

        var assetRouteData:MutableLiveData<AssetRoute> = MutableLiveData()
        RetrofitServiceBuilder.getService(LocationService::class.java)
                .getAssetRoute(assetId)
                .enqueue(object:Callback<AssetRoute>{
                    override fun onResponse(call: Call<AssetRoute>, response: Response<AssetRoute>) {

                        if(response.isSuccessful && response.code() == 200){
                            assetRouteData.postValue(response.body())
                        }
                    }

                    override fun onFailure(call: Call<AssetRoute>, t: Throwable) {
                    }

                })
        return assetRouteData
    }

    /**
     * Method to return asset geofence
     * @param assetIds {MutableList<String>}
     * @return list of geofence {List<AssetGeofence>}
     */
    fun getAssetsGeofence(assetIds: MutableList<String>):MutableLiveData<List<AssetGeofence>>{

        var assetsGeofence:MutableLiveData<List<AssetGeofence>> = MutableLiveData()
        RetrofitServiceBuilder.getService(LocationService::class.java).getAssetsGeofence(
                assetIds
        )
                .enqueue(object : Callback<List<AssetGeofence>> {
                    override fun onResponse(
                            call: Call<List<AssetGeofence>>,
                            response: Response<List<AssetGeofence>>
                    ) {
                        if (response.isSuccessful) {
                            if (response?.body()?.isNotEmpty()!!) {
                                assetsGeofence.postValue(response.body())
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<AssetGeofence>>, t: Throwable) {
                    }

                })

        return assetsGeofence
    }

    fun saveAssetRoute(assetId: String,assetRoute: AssetRoute,uiWork: () -> Unit){
        RetrofitServiceBuilder.getService(LocationService::class.java)
                .saveAssetRoute(assetId,assetRoute)
                .enqueue(object:Callback<ResponseBody>{
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                        if(response.isSuccessful && response.code() == 200){
                            uiWork()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    }

                })
    }

    fun saveAssetGeofence(assetId: String,assetGeofence: AssetGeofence,uiWork: ()-> Unit){
        RetrofitServiceBuilder.getService(LocationService::class.java).saveAssetGeofence(
                assetId,
                assetGeofence
        )
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                    ) {

                        if (response.isSuccessful) {
                            uiWork()
                        }

                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        println("raghul geofence ${t.localizedMessage} ")

                    }

                })
    }

    /**
     * Method to get the asset route
     *
     */
    fun getLocationRoute(startPoint: Point,destination:Point,uiWork: (body: DirectionsRoute) -> Unit){
        var client = MapboxDirections.builder().
        accessToken(context.getString(R.string.map_box_token))
                .origin(startPoint)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .build()
        client.enqueueCall(object : Callback<DirectionsResponse> {
            override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {

                println(response.isSuccessful)
                var body = response.body()
                if (body == null) {
                    println("No route")
                    return
                }

                uiWork(body.routes()[0])
            }

            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
            }

        })
    }


}
