package com.raghul.asset_tracker.model

data class  Notification(
val message:String,
val assetId:String,
val asset: Asset?
)