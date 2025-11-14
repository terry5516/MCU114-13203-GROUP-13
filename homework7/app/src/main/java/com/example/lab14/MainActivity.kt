package com.example.lab14

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.TravelMode
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var btnWalking: Button
    private lateinit var btnDriving: Button
    private lateinit var btnBicycling: Button

    // 固定起點和終點
    private val taipei101 = LatLng(25.033611, 121.565000)    // 台北101
    private val taipeiStation = LatLng(25.047924, 121.517081) // 台北車站

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && requestCode == 0) {
            val allGranted = grantResults.all {
                it == PackageManager.PERMISSION_GRANTED
            }
            if (allGranted) {
                setupMap()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 初始化按鈕
        btnWalking = findViewById(R.id.btnWalking)
        btnDriving = findViewById(R.id.btnDriving)
        btnBicycling = findViewById(R.id.btnBicycling)

        // 設置按鈕點擊監聽器
        btnWalking.setOnClickListener { drawRoute(TravelMode.WALKING, Color.RED, "步行") }
        btnDriving.setOnClickListener { drawRoute(TravelMode.DRIVING, Color.BLUE, "開車") }
        btnBicycling.setOnClickListener { drawRoute(TravelMode.BICYCLING, Color.GREEN, "騎自行車") }

        loadMap()
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        val isAccessFineLocationGranted = ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val isAccessCoarseLocationGranted = ActivityCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (isAccessFineLocationGranted && isAccessCoarseLocationGranted) {
            setupMap()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0
            )
        }
    }

    private fun setupMap() {
        try {
            val hasLocationPermission = ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (hasLocationPermission) {
                googleMap.isMyLocationEnabled = true
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }

        // 添加標記
        googleMap.addMarker(MarkerOptions().position(taipei101).title("台北101"))
        googleMap.addMarker(MarkerOptions().position(taipeiStation).title("台北車站"))

        // 預設顯示步行路線
        drawRoute(TravelMode.WALKING, Color.RED, "步行")

        // 移動視角到中間點
        val centerPoint = LatLng(
            (taipei101.latitude + taipeiStation.latitude) / 2,
            (taipei101.longitude + taipeiStation.longitude) / 2
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerPoint, 13f))
    }

    private fun drawRoute(travelMode: TravelMode, color: Int, modeName: String) {
        clearMap()
        drawRouteWithMode(taipei101, taipeiStation, travelMode, color, modeName)
    }

    private fun clearMap() {
        googleMap.clear()
        // 重新添加標記
        googleMap.addMarker(MarkerOptions().position(taipei101).title("台北101"))
        googleMap.addMarker(MarkerOptions().position(taipeiStation).title("台北車站"))
    }

    private fun drawRouteWithMode(origin: LatLng, destination: LatLng, travelMode: TravelMode, color: Int, modeName: String) {
        val apiKey = "AIzaSyALYyifEb9_6kYcvc4JZzKcfsed-GDQdsM"

        val geoApiContext = GeoApiContext.Builder()
            .apiKey(apiKey)
            .build()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val directionsResult = DirectionsApi.newRequest(geoApiContext)
                    .origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                    .destination(com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                    .mode(travelMode)  // 使用傳入的交通工具模式
                    .await()

                CoroutineScope(Dispatchers.Main).launch {
                    if (directionsResult.routes.isNotEmpty()) {
                        val route = directionsResult.routes[0]
                        val decodedPath = PolyUtil.decode(route.overviewPolyline.encodedPath)

                        val polylineOptions = PolylineOptions()
                            .addAll(decodedPath)
                            .color(color)
                            .width(12f)

                        googleMap.addPolyline(polylineOptions)

                        // 顯示路線資訊
                        val distance = route.legs[0].distance.humanReadable
                        val duration = route.legs[0].duration.humanReadable
                        println("$modeName - 距離: $distance, 時間: $duration")

                    } else {
                        drawFallbackLine(origin, destination, color)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                CoroutineScope(Dispatchers.Main).launch {
                    drawFallbackLine(origin, destination, color)
                }
            }
        }
    }

    private fun drawFallbackLine(origin: LatLng, destination: LatLng, color: Int) {
        val polylineOpt = PolylineOptions()
            .add(origin)
            .add(destination)
            .color(color)
            .width(8f)
        googleMap.addPolyline(polylineOpt)
    }

    private fun loadMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
}