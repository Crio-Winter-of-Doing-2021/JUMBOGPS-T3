package com.raghul.asset_tracker.model

data class AssetGeofence (
        val assetId: String,
        val coordinates: MutableList<Coordinate>
        )