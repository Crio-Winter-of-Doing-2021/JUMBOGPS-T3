package com.raghul.asset_tracker.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.raghul.asset_tracker.config.RetrofitServiceBuilder
import com.raghul.asset_tracker.model.Asset
import com.raghul.asset_tracker.service.AssetService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AssetRepository(private  val context: Context) {

    /**
     * Method to get the assets
     */
    fun getAssets():MutableLiveData<MutableList<Asset>>{
       var assets: MutableLiveData<MutableList<Asset>>  = MutableLiveData<MutableList<Asset>>()
        RetrofitServiceBuilder.getService(AssetService::class.java).getAssets().enqueue(object : Callback<MutableList<Asset>> {
            override fun onResponse(call: Call<MutableList<Asset>>, response: Response<MutableList<Asset>>) {
                if(response.isSuccessful){
                    assets.postValue(response.body() !!)
                }
            }
            override fun onFailure(call: Call<MutableList<Asset>>, t: Throwable) {
            }

        })

        return assets
    }

    /**
     * Method to delete the asset
     * @param asset {Asset}
     */
    fun deleteAsset(asset:Asset,uiWork:()->Unit){
        RetrofitServiceBuilder.getService(AssetService::class.java).deleteAsset(asset.id)
                .enqueue(object: Callback<Unit>{
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        uiWork()
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                    }

                })
    }

}