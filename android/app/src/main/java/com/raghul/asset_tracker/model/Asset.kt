package com.raghul.asset_tracker.model

data class Asset(
        val id: String,
        val assetName: String,
        val assetType: String,
        val locations: List<Location>
)