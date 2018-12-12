package com.example.hicabbie.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.example.hicabbie.R
import com.example.hicabbie.data.response.ResponseLocation
import com.example.hicabbie.service.ServiceCar
import com.example.hicabbie.ui.base.BaseActivity
import com.example.hicabbie.utils.click
import kotlinx.android.synthetic.main.activity_main.*
import com.example.hicabbie.service.ServiceCar.MyBinder
import android.os.IBinder
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.util.Log
import com.example.hicabbie.utils.PermissionManager
import com.example.hicabbie.utils.longToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject


class MainActivity : BaseActivity(), HomeView, OnMapReadyCallback {


    @Inject
    lateinit var presenter: PresenterHome

    override fun onError(msg: String) {

    }

    override fun updateButtonStatus(started: Boolean) {
        if (started) ivBtnStart.setImageResource(R.drawable.ic_stop_24) else
            ivBtnStart.setImageResource(R.drawable.ic_start_24)
    }

    override fun updateLocation(loc: ResponseLocation) {
        if (loc.status.equals("success", true))
            showMarkerOnMap(loc.latitude, loc.longitude)
    }

    private var mGoogleMap: GoogleMap? = null
    private val REQUEST_CODE_LOCATION: Int = 44

    private val lat = 28.2252131
    private val lng = 77.9732094
    override fun onMapReady(mMap: GoogleMap?) {
        mGoogleMap = mMap
        showMarkerOnMap(lat, lng) // default
        permissionCheck()
    }

    private fun permissionCheck() {
        if (PermissionManager.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            getLocationFromMap()
        } else {
            PermissionManager.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_CODE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocationFromMap() {
        mGoogleMap?.apply {
            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = false
            uiSettings.isMapToolbarEnabled = false
        }
        if (presenter.checkLastLocation() == null) // this is goinf to check last location and if no null will update view
            mFusedLocationClient?.lastLocation?.addOnSuccessListener { loc ->
                if (loc != null) showMarkerOnMap(loc.latitude, loc.longitude) else
                    showMarkerOnMap(lat, lng)
            }
    }

    private fun showMarkerOnMap(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)
        val marker = MarkerOptions().apply {
            position(latLng)
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        }
        mGoogleMap?.apply {
            clear()
            addMarker(marker)
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f))
        }
    }


    private var mFusedLocationClient: FusedLocationProviderClient? = null

    override fun gtContentView() = R.layout.activity_main

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent?) {
        //to start
        ivBtnStart.click { presenter.startPolling() }
    }

    override fun onStart() {
        super.onStart()
        ServiceCar.getNewIntent(this).also {
            startService(it)
            bindService(it, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (bounded) {
            unbindService(connection)
            bounded = false
        }
        presenter.onStop(this)
    }

    private var bounded = false
    private val connection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName) {
            bounded = false
        }

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val myBinder = service as MyBinder
            bounded = true
            presenter.service(myBinder.getService())
            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this@MainActivity)
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
        }
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocationFromMap()
                } else {
                    longToast("This permission is mandatory for this app to work properly")
                }
            }
        }

    }


}

interface OnUpdateListner {
    fun update(it: ResponseLocation)
}


