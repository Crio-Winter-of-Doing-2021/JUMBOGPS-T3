package com.raghul.asset_tracker.utils

import com.mapbox.geojson.Point
import com.raghul.asset_tracker.model.AssetGeofence
import com.raghul.asset_tracker.model.Coordinate
import com.raghul.asset_tracker.model.OrderedCoordinate

object PointTranslator {

    fun TranslateToCoordinates(assetId:String,points : MutableList<Point>) : AssetGeofence{
        var listCoordinates : MutableList<Coordinate> = mutableListOf()
        points.forEach {
            point->
            listCoordinates.add(Coordinate(point.latitude(),point.longitude()))
        }
        return AssetGeofence(assetId,listCoordinates)
    }

    fun translateToCoordinates(points:List<Point>):List<OrderedCoordinate>{
        val listCoordinates : MutableList<OrderedCoordinate> = mutableListOf()
        var item = 1;
        points.forEach {
            point ->
            listCoordinates.add(OrderedCoordinate(Coordinate(point.latitude(),point.longitude()),item))
            item++
        }
        return listCoordinates
    }
}