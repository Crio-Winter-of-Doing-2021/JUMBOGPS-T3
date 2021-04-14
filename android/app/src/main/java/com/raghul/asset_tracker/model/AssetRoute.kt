package com.raghul.asset_tracker.model

data class AssetRoute(
        val assetId:String,
        val src:Coordinate,
        val dest:Coordinate,
        val routes:List<OrderedCoordinate>,
        var isDestinationReached:Boolean?
)