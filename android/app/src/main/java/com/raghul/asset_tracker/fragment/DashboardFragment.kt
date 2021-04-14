package com.raghul.asset_tracker.fragment

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.gson.Gson
import com.mapbox.android.telemetry.NavigationLocationData
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.core.constants.Constants.PRECISION_5
import com.mapbox.core.constants.Constants.PRECISION_6
import com.mapbox.geojson.*
import com.mapbox.geojson.gson.GeometryGeoJson
import com.mapbox.geojson.utils.GeoJsonUtils
import com.mapbox.geojson.utils.PolylineUtils
import com.mapbox.mapboxsdk.annotations.Polyline
import com.mapbox.mapboxsdk.annotations.PolylineOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngBounds
import com.mapbox.mapboxsdk.constants.GeometryConstants
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.*
import com.mapbox.turf.*
import com.mapbox.turf.TurfConstants.UNIT_KILOMETERS
import com.raghul.asset_tracker.R
import com.raghul.asset_tracker.config.RetrofitServiceBuilder
import com.raghul.asset_tracker.model.*
import com.raghul.asset_tracker.repository.LocationRepository
import com.raghul.asset_tracker.repository.NotificationRepository
import com.raghul.asset_tracker.repository.ReferenceRepository
import com.raghul.asset_tracker.service.LocationService
import com.raghul.asset_tracker.utils.CommonConstants
import com.raghul.asset_tracker.utils.ImageHelper
import com.raghul.asset_tracker.utils.NotificationUtils
import com.raghul.asset_tracker.utils.PointTranslator
import net.mastrgamr.mbmapboxutils.PolyUtil
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.RandomAccess


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment(),MapboxMap.OnMapClickListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit  var mapView: MapView
    private  var mapboxMap: MapboxMap?=null
    private final val AssetTruckIconId = "truck"
    private final val AssetHumanIconId = "human"
    private lateinit var parentViewLayout: RelativeLayout
    private lateinit var isIncludeTimecb: MaterialCheckBox
    private lateinit var startDate:EditText
    private lateinit var assetCheckBoxLayout: LinearLayout
    private lateinit var refTermRepository: ReferenceRepository
    private lateinit var locationRepository: LocationRepository
    private lateinit var styleMap: Style
    private lateinit var symbols:MutableMap<Symbol, Asset>
    private lateinit var  symbolManager: SymbolManager
    private lateinit var lineManager: LineManager
    private lateinit var searchText: TextInputEditText
    private lateinit var searchButton: MaterialButton
    private lateinit var refValues:MutableMap<CheckBox, RefTerm>
    private lateinit var searchCardView:MaterialCardView
    private lateinit var  circleSeekbar:SeekBar
    private lateinit var seekbarTextView: TextView
    private  var polygonPoint:Point? = null
    private lateinit var fillManager: FillManager
    private lateinit var geofenceList: MutableMap<String, Fill>
    private  var assetId: String = ""
    private  var radius: Double = 50.0
    private lateinit var geoFenceLayout: MaterialCardView
    private lateinit var geoPoints: MutableMap<String, MutableList<Point>>
    private lateinit var setGeofenceButton: MaterialButton
    private lateinit var startTime:EditText
    private lateinit var endDate:EditText
    private lateinit var endTime:EditText
    private lateinit var geofenceCancelButton: MaterialButton
    private var markerSymbol:Symbol? = null
    private lateinit var assetPopup: AlertDialog
    private  var assetSymbol:Symbol?=null
    private var assetSymbolRouteline:Line?=null
    private var isGeoRoute = false
    private lateinit var setDirectionButton:MaterialButton
    private var destPoint:Point?=null
    private var isDirectionButtonEnabled = true
    private var assetLinePoints:MutableList<Point>?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val seekBarChangeListener: OnSeekBarChangeListener = object: OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            if(assetId.isNotBlank()){
                polygonPoint?.let { drawPolygonFromPoint(assetId, it, p1.toDouble()) }
                seekbarTextView.text = "${p1/10} km"
            }

        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
        }

    }

    private val setGeofenceListener : View.OnClickListener = View.OnClickListener {
        if(geoPoints[assetId] != null){
            var assetGeofence = PointTranslator.TranslateToCoordinates(
                assetId,
                points = geoPoints[assetId]!!
            )

            locationRepository.saveAssetGeofence(assetId,assetGeofence) {
                Snackbar.make(parentViewLayout, "Geofence is set successfully", Snackbar.LENGTH_LONG).show()
                geoFenceLayout.visibility = View.GONE
                assetId = ""
            }


        }

    }



    private fun searchSetup(view: View):Unit{
        assetCheckBoxLayout = view.findViewById(R.id.asset_checkbox_layout)
        context?.let {
            refTermRepository = ReferenceRepository(it)
            locationRepository = LocationRepository(it)
        }
        geoPoints = mutableMapOf()
        setGeofenceButton = view.findViewById(R.id.set_geofence_button)
        geoFenceLayout = view.findViewById(R.id.set_geofence_layout)
        setGeofenceButton.setOnClickListener(setGeofenceListener)
        polygonPoint = Point.fromLngLat(80.28373, 13.06592)
         searchCardView = view?.findViewById<MaterialCardView>(R.id.search_layout)
        circleSeekbar = view?.findViewById(R.id.circle_seekbar)
        circleSeekbar.setOnSeekBarChangeListener(seekBarChangeListener)
        seekbarTextView = view.findViewById(R.id.circle_radius)
        setDirectionButton = view.findViewById(R.id.set_direction_button)
        refValues = mutableMapOf()
        loadDropdownFields()
        isIncludeTimecb = view.findViewById(R.id.asset_include_time)
        startDate = view.findViewById(R.id.asset_start_date_holder)
        startDate.setOnClickListener(dateClickListener)
        startTime = view.findViewById(R.id.asset_start_time_holder)
        startTime.setOnClickListener(dateClickListener)
        endTime = view.findViewById(R.id.asset_end_time_holder)
        endTime.setOnClickListener(dateClickListener)
        endDate = view.findViewById(R.id.asset_end_date_holder)
        endDate.setOnClickListener(dateClickListener)
        geofenceCancelButton = view.findViewById(R.id.geofence_cancel_button)
        geofenceCancelButton.setOnClickListener(geofenceCancelListener)
        isIncludeTimecb.setOnClickListener{ v-> if(isIncludeTimecb.isChecked){
                startDate.visibility = View.VISIBLE
                startTime.visibility = View.VISIBLE
                endDate.visibility = View.VISIBLE
                endTime.visibility = View.VISIBLE
        }else{
            startDate.visibility = View.GONE
            startTime.visibility = View.GONE
            endTime.visibility = View.GONE
            endDate.visibility = View.GONE
        }
        }
    }

    private fun loadDropdownFields(){
        refTermRepository.getReferenceValues(getString(R.string.key_asset_type))
                .observe(viewLifecycleOwner, { values ->
                    if (values.isNotEmpty()) {
                        values.forEach { value ->
                            val checkBox = MaterialCheckBox(context)
                            checkBox.text = value.name
                            refValues[checkBox] = value
                            var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                                0,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                1f
                            )
                            assetCheckBoxLayout.addView(checkBox, params)
                        }
                    }
                })
    }

    private val searchButtonListener : View.OnClickListener = View.OnClickListener {

            _->
        var assetTypes: MutableList<String>? = null
        if(refValues.isNotEmpty()){
            assetTypes = mutableListOf()
        }
        refValues.forEach{
            if(it.key.isChecked){
                assetTypes?.add(it.value.id);
            }
        }
        var startDateTime:String? = null
        if(startDate.text.toString().isNotBlank() && startTime.text.toString().isNotBlank()){
            startDateTime = "${startDate.text.toString()} ${startTime.text.toString()}"
        }
        var endDateTime:String?=null
        if(endDate.text.toString().isNotBlank() && endTime.text.toString().isNotBlank()){
            endDateTime = "${endDate.text.toString()} ${endTime.text.toString()}"
        }
        if(startDateTime!=null && endDateTime == null){
            endDateTime = startDateTime
        }
        createAssetCurrentStats(searchText.text.toString(), assetTypes,startDateTime,endDateTime)
        startDate.setText("")
        startTime.setText("")
        endDate.setText("")
        endTime.setText("")

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View =  inflater.inflate(R.layout.fragment_dashboard, container, false)
        searchCardView = view?.findViewById<MaterialCardView>(R.id.search_layout)
        geofenceList = mutableMapOf()
        searchSetup(view)
        searchText = view.findViewById(R.id.asset_search)
        searchButton = view.findViewById(R.id.asset_search_button)
        searchButton.setOnClickListener(searchButtonListener)
         symbols= mutableMapOf()
        mapView = view.findViewById(R.id.assetMapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync{
            mapboxMap = it
            mapboxMap!!.addOnMapClickListener(this)
            it.setStyle(
                Style.Builder()
                    .fromUrl(getString(R.string.mapbox_map_style_url))
                    .withImage(AssetTruckIconId, ImageHelper.getBitmap(requireContext(),R.drawable.asset_truck))
                    .withImage(AssetHumanIconId, ImageHelper.getBitmap(requireContext(),R.drawable.asset_salesperson))
                        .withImage(CommonConstants.MARKER, BitmapFactory.decodeResource(
                                this.resources, R.drawable.mapbox_marker_icon_default))
            ) { style ->
                styleMap = style
                symbolManager = SymbolManager(mapView, mapboxMap!!, style)
                 lineManager = LineManager(mapView, mapboxMap!!, style)
                fillManager = FillManager(mapView, mapboxMap!!, style)



                symbolManager.iconAllowOverlap = true
                symbolManager.textAllowOverlap = true


               var argument: Bundle? = arguments
                if(argument == null){
                    isGeoRoute = false
                    setDirectionButton.visibility = View.GONE
                    activity?.title="Dashboard"
                    searchCardView.visibility = View.VISIBLE
                    fillManager.addClickListener{
                        println("fill manager listener")
                       Snackbar.make(parentViewLayout, "Boundary for asset", Snackbar.LENGTH_LONG).show()
                    }
                    fillManager.addDragListener(object : OnFillDragListener {
                        override fun onAnnotationDragStarted(annotation: Fill?) {
                            println("drag started")
                        }

                        override fun onAnnotationDrag(annotation: Fill?) {
                            println("in drag")
                        }

                        override fun onAnnotationDragFinished(annotation: Fill?) {
                            println("drag finished")
                            if(assetId.isNotBlank()){
                                geofenceList[assetId] = annotation!!
                                geoPoints[assetId] = TurfMeta.coordAll(annotation!!.geometry, false)
                            }

                        }

                    })
                    addSymbolClickListener()
                    createAssetCurrentStats()
                }else if(CommonConstants.TIMELINE_BUNDLE == argument.getString(CommonConstants.MODULE)){
                    isGeoRoute = false
                    setDirectionButton.visibility = View.GONE
                    argument.getString("assetId").let { assetId->
                        if (assetId != null) {
                            searchCardView.visibility = View.GONE
                            createAssetTimeline(mapView, mapboxMap!!, style, assetId)
                        }
                    }
                }else if(CommonConstants.GEOROUTE_BUNDLE == argument.getString(CommonConstants.MODULE)){
                    isGeoRoute = true
                    setDirectionButton.visibility = View.VISIBLE
                    searchCardView.visibility = View.GONE
                    var assetValue: Asset = Gson().fromJson(argument.getString(CommonConstants.GEOROUTE_DATA),Asset::class.java)
                    createAssetRoute(assetValue)
                }


            }
        }
        return view
    }

    private fun createAssetRoute(asset:Asset){
        var latVal = asset.locations[0].lat
        var lonVal = asset.locations[0].lon
        setDirectionButton.isClickable = true
        setDirectionButton.isEnabled = true
        assetId = asset.id
        mapboxMap?.cameraPosition = CameraPosition.Builder().target(LatLng(latVal,lonVal))
                .zoom(14.0)
                .build()
        var assetIconId = when(asset.assetType){
            "truck"->AssetTruckIconId
            "salesperson"->AssetHumanIconId
            else -> AssetHumanIconId
        }
        if(assetSymbol != null){
            symbolManager.delete(assetSymbol)
        }
        locationRepository.getAssetRoute(assetId)
                .observe(viewLifecycleOwner,{assetRouteData->
                    if(assetRouteData!=null){
                        if(assetRouteData.isDestinationReached != null && assetRouteData.isDestinationReached == false){
                            isDirectionButtonEnabled = false
                            setDirectionButton.text = "Current Direction"
                            setDirectionButton.isEnabled = false
                            setDirectionButton.isClickable = false
                        }
                        drawAssetRouteFromRouteData(assetRouteData,latVal,lonVal,asset)
                    }else{

                    }
                })

        polygonPoint = Point.fromLngLat(lonVal,latVal)

       assetSymbol= symbolManager.create(SymbolOptions().withIconImage(assetIconId)
                .withIconSize(0.07f).withGeometry(Point.fromLngLat(lonVal,latVal)))
    }

    private fun drawAssetRouteFromRouteData(assetRoute: AssetRoute,latVal:Double,lonVal:Double,asset: Asset){
        if(assetSymbolRouteline != null){
            lineManager.delete(assetSymbolRouteline)
        }
        var mutableRouteList:MutableList<Point> = mutableListOf()
        val mutableLatlanList:MutableList<LatLng> = mutableListOf()
        for (coordinate in assetRoute.routes) {
            mutableRouteList.add(Point.fromLngLat(coordinate.coordinate.lon,coordinate.coordinate.lat))
            mutableLatlanList.add(LatLng(coordinate.coordinate.lat,coordinate.coordinate.lon))

        }
        assetSymbolRouteline = lineManager.create(LineOptions().withGeometry(LineString.fromLngLats(mutableRouteList))
                .withLineColor("#FF0000").withLineWidth(3f))
        if(markerSymbol != null){
            symbolManager.delete(markerSymbol)
        }
        var lastPoint = mutableRouteList[mutableRouteList.size-1]
        markerSymbol = symbolManager.create(SymbolOptions().withIconSize(1f).withGeometry(lastPoint)
                .withIconImage(CommonConstants.MARKER))

        val toleranceValue:Double = PreferenceManager.getDefaultSharedPreferences(context).getString("tolerance","0")
            ?.toDouble()?:0.0
       if(PolyUtil.isLocationOnPath(LatLng(latVal,lonVal),mutableLatlanList,false,toleranceValue)){
           Snackbar.make(parentViewLayout,"${asset.assetName} is travelling in the actual path",Snackbar.LENGTH_LONG).show()
       }else{
           var messageText:String =  "${asset.assetName} is travelling away from the actual path"
           Snackbar.make(parentViewLayout,messageText,Snackbar.LENGTH_LONG).show()
           NotificationRepository(requireContext()).saveNotification(Notification(messageText,asset.id,null))
           NotificationUtils("georoute",Random().nextInt(),requireContext())
               .createNotification("georoute",messageText)


       }


    }
    private fun addSymbolClickListener(){
        symbolManager.addClickListener { u ->
            var asset = symbols[u]
            val popupView:View = LayoutInflater.from(context).inflate(
                R.layout.asset_popup_information,
                parentViewLayout,
                false
            )
            popupView.findViewById<TextView>(R.id.popup_assetname).text = asset?.assetName
            popupView.findViewById<TextView>(R.id.popup_assettype).text = asset?.assetType
            popupView.findViewById<TextView>(R.id.popup_assettime).text = asset?.locations?.get(0)?.time
            popupView.findViewById<MaterialButton>(R.id.asset_view_timeline).setOnClickListener{

                var dashboardFragment = DashboardFragment()
                var argumentValue:Bundle = Bundle()
                argumentValue.putString("assetId", asset?.id)
                argumentValue.putString(CommonConstants.MODULE,CommonConstants.TIMELINE_BUNDLE)
                dashboardFragment.arguments = argumentValue
                parentFragmentManager?.beginTransaction()?.
                replace(R.id.dashboard_fragment, dashboardFragment)?.addToBackStack(
                        dashboardFragment.javaClass.name
                )?.commit()
                assetPopup.dismiss()
            }

            val lat: Double = asset?.locations?.get(0)?.lat!!
            var lon : Double = asset?.locations?.get(0)?.lon!!
            popupView.findViewById<MaterialButton>(R.id.asset_geofence).setOnClickListener{
                seekbarTextView.text = "${radius.toInt() / 10} km"
                drawPolygonFromPoint(assetId, Point.fromLngLat(lon, lat), radius)
                geoFenceLayout.visibility = View.VISIBLE
                assetPopup.dismiss()
            }
            popupView.findViewById<MaterialButton>(R.id.asset_georoute).setOnClickListener{
                var dashboardFragment = DashboardFragment()
                var argumentValue:Bundle = Bundle()
                argumentValue.putString(CommonConstants.MODULE,CommonConstants.GEOROUTE_BUNDLE)
                argumentValue.putString(CommonConstants.GEOROUTE_DATA,Gson().toJson(asset,Asset::class.java))
                dashboardFragment.arguments = argumentValue
                parentFragmentManager?.beginTransaction()?.
                replace(R.id.dashboard_fragment, dashboardFragment)?.addToBackStack(
                        dashboardFragment.javaClass.name
                )?.commit()
                assetPopup.dismiss()
            }
            assetId = asset!!.id

            circleSeekbar.progress = radius.toInt()

            val assetImage = popupView.findViewById<ImageView>(R.id.popup_assetimage)
            when(asset?.assetType){
                "truck" -> assetImage.setImageResource(R.drawable.asset_truck)
                "salesperson" -> assetImage.setImageResource(R.drawable.asset_salesperson)
                else -> assetImage.setImageResource(R.drawable.asset_truck)

            }


                assetPopup =   MaterialAlertDialogBuilder(requireContext())
                        .setView(popupView)
                        .setPositiveButton(
                            getString(R.string.popup_ok_button)
                        ) { p0, _ -> p0?.dismiss() }
                        .show()


        }

    }


    private fun drawPolygonFromPoint(assetId: String, point: Point, radius: Double){
        polygonPoint = point
            var polygonArea = getPolygon(point, radius / 10, UNIT_KILOMETERS)

        var fillValue:Fill? = geofenceList[assetId]
            if(fillValue!= null){
                fillValue!!.geometry = polygonArea
                fillManager.update(fillValue)
                geofenceList[assetId] = fillValue
                geoPoints[assetId] = TurfMeta.coordAll(polygonArea, false)
            }else{
                fillValue = fillManager.create(
                    FillOptions().withFillColor("#000000").withFillOpacity(
                        0.4f
                    )
                        .withGeometry(polygonArea).setDraggable(true)
                )
                geofenceList[assetId] = fillValue
                geoPoints[assetId] = TurfMeta.coordAll(polygonArea, false)
            }


    }

    private fun getPolygon(point: Point, radius: Double, units: String): Polygon {
      return TurfTransformation.circle(point, radius, units)
    }

    private fun createAssetTimeline(
        mapView: MapView,
        mapboxMap: MapboxMap,
        style: Style,
        assetId: String
    ) {
        locationRepository.getAssetCurrentTimeline(assetId).observe(viewLifecycleOwner, {
            it?.let {
                var latLong = LatLng()
                latLong.latitude = it.locations[0].lat
                latLong.longitude = it.locations[0].lon
                var points: MutableList<Point> = getCoordinates(it)
                var latLngList: MutableList<LatLng> = mutableListOf()
                points.forEach { point ->
                    latLngList.add(LatLng(point?.latitude(), point?.longitude()))
                }
                mapboxMap.easeCamera(
                    newLatLngBounds(
                        LatLngBounds.Builder()
                            .includes(latLngList)
                            .build(), 75
                    ), 1000
                )
                var iconId = if (it.assetType == "truck") AssetTruckIconId else AssetHumanIconId
                var symbol = symbolManager.create(
                    SymbolOptions().withGeometry(
                        Point.fromLngLat(
                            it.locations[0].lon,
                            it.locations[0].lat
                        )
                    ).withIconImage(
                        iconId
                    )
                        .withIconSize(0.07f)
                )
                symbolManager.create(
                    SymbolOptions().withGeometry(
                        Point.fromLngLat(
                            it.locations[it.locations.size - 1].lon,
                            it.locations[it.locations.size - 1].lat
                        )
                    ).withIconImage(
                        iconId
                    )
                        .withIconSize(0.07f)
                )
                var lines: Line = lineManager.create(
                    LineOptions().withGeometry(
                        LineString.fromLngLats(
                            points
                        )
                    ).withLineColor("#9932CC").withLineWidth(3f).withLineOffset(1f)
                )
            }
        })

    }

    private fun getCoordinates(asset: Asset):MutableList<Point>{
      var  points: MutableList<Point> = mutableListOf()
        asset.locations.let {
            it.forEach { location ->
                points.add(Point.fromLngLat(location.lon, location.lat))
            }
        }
        return points

    }
    private fun loadSymbols(
        symbols: MutableMap<Symbol, Asset>,
        assets: List<Asset>,
        symbolManager: SymbolManager
    ){
        assets.forEach{ asset->
            var iconId = if(asset.assetType == "truck") AssetTruckIconId else AssetHumanIconId
            var symbol = symbolManager.create(
                SymbolOptions().withGeometry(
                    Point.fromLngLat(
                        asset.locations[0].lon,
                        asset.locations[0].lat
                    )
                ).withIconImage(
                    iconId
                )
                    .withIconSize(0.07f)
            )
            symbols[symbol] = asset

        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parentViewLayout = view.findViewById(R.id.dashboard_fragment)
        super.onViewCreated(view, savedInstanceState)

    }
    private fun createAssetCurrentStats(
        text: String? = null,
        assetTypes: MutableList<String>? = null,
        startDateTime: String?=null,
        endDateTime: String?=null,
        size: Int?=PreferenceManager.getDefaultSharedPreferences(context).getString("marker","100")?.toInt()
    ){
        symbols.forEach{
            symbolManager.delete(it.key)
        }
        geofenceList.forEach{
            fillManager.delete(it.value)
        }
        println("raghul from test$size")

        symbols = mutableMapOf()
        println("rag" + symbols.size)
        println(text)
        locationRepository.getAssetsCurrentLocation(text, assetTypes
        ,startDateTime,endDateTime,size).observe(viewLifecycleOwner, { assets ->
            if (assets.isNotEmpty()) {
                println("inside")
                var assetIds = mutableListOf<String>()
                var latLngList: MutableList<LatLng> = mutableListOf()
                assets.forEach { asset ->
                    latLngList.add(LatLng(asset?.locations[0].lat, asset?.locations[0].lon))
                    assetIds.add(asset.id)
                }
                if (latLngList.size > 1) {
                    mapboxMap?.easeCamera(
                        newLatLngBounds(
                            LatLngBounds.Builder()
                                .includes(latLngList)
                                .build(), 75
                        ), 1000
                    )
                } else if (latLngList.size == 1) {
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder().target(latLngList[0]).build()
                    )
                }


                locationRepository.getAssetsGeofence(assetIds).observe(viewLifecycleOwner,{
                    if(it.isNotEmpty()){
                        translateToPoints(assets, it)
                    }
                })
                loadSymbols(symbols, assets, symbolManager)
            }
        })


    }

    private fun translateToPoints(assets: List<Asset>, assetGeofences: List<AssetGeofence>){
        assetGeofences.forEach { assetGeofence ->
            translateToPoints(assetGeofence)

        }
        assets.forEach {
            var points = geoPoints[it.id]
            if(points != null){
                var poly =  Polygon.fromLngLats(mutableListOf(points))
                if(TurfJoins.inside(
                        Point.fromLngLat(it.locations[0].lon, it.locations[0].lat),
                        poly!!
                    )){
                    println("within geofence")
                }else{
                    println("Away From geofence")
                    val geofenceString = "${it.assetName} is away from geofence"
                    NotificationRepository(requireContext())
                        .saveNotification(Notification(message = geofenceString,assetId=it.id,null))
                    NotificationUtils(getString(R.string.channel_geofence),Random().nextInt(),requireContext()).
                    createNotification("Geofence",geofenceString)
                }
            }
        }
    }

    private fun translateToPoints(geofence: AssetGeofence){
        var points:MutableList<Point> = mutableListOf()
        geofence.coordinates.forEach {
            points.add(Point.fromLngLat(it.lon, it.lat))
        }
        var fill = drawCircleFromPoints(points)
        geofenceList[geofence.assetId] = fill
        geoPoints[geofence.assetId] = points



    }

    private fun drawCircleFromPoints(points: MutableList<Point>):Fill{
       var poly =  Polygon.fromLngLats(mutableListOf(points))
        return fillManager.create(
            FillOptions().withGeometry(poly).withFillColor("#006400").withFillOpacity(
                0.4f
            )
                .setDraggable(false)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mapboxMap != null){
            mapboxMap?.removeOnMapClickListener(this)
        }

    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    private var dateClickListener:View.OnClickListener = View.OnClickListener {


        when(it.id){
            R.id.asset_start_date_holder ->
                datePicker(CommonConstants.START_DATE)
            R.id.asset_start_time_holder ->
                timePicker(CommonConstants.START_TIME)
            R.id.asset_end_date_holder->
                datePicker(CommonConstants.END_DATE)
            R.id.asset_end_time_holder->
                timePicker(CommonConstants.END_TIME)

        }
    }


    private fun datePicker(dateMode: String){
            var picker =  MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()
            picker.show(this.parentFragmentManager, "Start Date")
            picker.addOnPositiveButtonClickListener {dateSelected->
                val simpleFormat = SimpleDateFormat("YYYY-MM-dd")
                val date = Date(dateSelected)
               val actualDate =  simpleFormat.format(date)
                if(CommonConstants.START_DATE == dateMode){
                    startDate.setText(actualDate)
                }else if(CommonConstants.END_DATE == dateMode){
                    endDate.setText(actualDate)
                }

            }


    }

    private fun timePicker(timeMode: String){

            var timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Select  time")
                .build()
            timePicker.show(this.parentFragmentManager, "Start Time")
            timePicker.addOnPositiveButtonClickListener{
                println(timePicker.hour)
                println(timePicker.minute)
                var zeroHour = ""
                var zeroMin = ""
                if(timePicker.hour <=9)
                {
                    zeroHour="0"
                }
                if(timePicker.minute <=9){
                    zeroMin = "0"
                }
                var time = "${zeroHour}${timePicker.hour}:${zeroMin}${timePicker.minute}:00"
                if(CommonConstants.START_TIME == timeMode){
                    startTime.setText(time)
                }else if(CommonConstants.END_TIME == timeMode){
                    endTime.setText(time)
                }
            }



    }


    private var geofenceCancelListener: View.OnClickListener = View.OnClickListener {
        fillManager.update(geofenceList[assetId])
        assetId=""
        geoFenceLayout.visibility = View.GONE
    }

    override fun onMapClick(point: LatLng): Boolean {
        if(markerSymbol != null){
            symbolManager.delete(markerSymbol)
        }
        if(isGeoRoute && isDirectionButtonEnabled){
            setDirectionButton?.setOnClickListener{
                if(assetId != null && assetLinePoints != null && assetLinePoints?.isNotEmpty() == true){

                   val routeCoordinates = PointTranslator.translateToCoordinates(assetLinePoints!!)
                    val src = Coordinate(polygonPoint!!.latitude(),polygonPoint!!.longitude())
                    val dest = Coordinate(destPoint!!.latitude(),destPoint!!.longitude())
                    var assetRoute = AssetRoute(assetId,src,dest,routeCoordinates,false)
                    locationRepository.saveAssetRoute(assetId,assetRoute){
                        Snackbar.make(parentViewLayout,"Geo Route Set", Snackbar.LENGTH_LONG).show()

                    }
                }

            }
            markerSymbol = symbolManager.create(SymbolOptions().withIconSize(1f).
            withGeometry(Point.fromLngLat(point.longitude,point.latitude)).withIconImage(CommonConstants.MARKER))
            getRoute(Point.fromLngLat(point.longitude,point.latitude))
        }

        return false
    }
   private fun getRoute(destination:Point){
       destPoint = destination
       locationRepository.getLocationRoute(polygonPoint!!,destination){body->
           if(assetSymbolRouteline != null){
               lineManager.delete(assetSymbolRouteline)
           }
           var  assetLineString = LineString.fromPolyline(body.geometry()!!,PRECISION_6)
           assetLinePoints = assetLineString?.coordinates() as MutableList<Point>
           assetSymbolRouteline = lineManager.create(LineOptions().
           withGeometry(LineString.fromLngLats(assetLinePoints!!))
                   .withLineColor("#9932CC")
                   .withLineWidth(4f))
       }



    }


}