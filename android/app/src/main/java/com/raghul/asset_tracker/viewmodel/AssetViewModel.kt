package com.raghul.asset_tracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.raghul.asset_tracker.model.Asset
import com.raghul.asset_tracker.repository.AssetRepository

class AssetViewModel(application: Application) : AndroidViewModel(application) {
    var assetRepository: AssetRepository = AssetRepository(application.applicationContext)
    var assets: MutableLiveData<MutableList<Asset>> = MutableLiveData()
    init {
       assets =  assetRepository.getAssets()
    }

    fun getAllAssets(): MutableLiveData<MutableList<Asset>>{
        return this.assets
    }
}