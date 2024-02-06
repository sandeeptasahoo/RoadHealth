package com.example.roadhealth2

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log

class LocationManagerImpl(context : Context, private val permissionManager: PermissionManager) {

    private val TAG="LocationManagerImpl"
    private val locationServiceManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    var gpsLocation: Location?=null
    var netLocation: Location?=null

    private val gpsLocationListener: LocationListener =
        LocationListener { location -> gpsLocation = location }

    private val networkLocationListener: LocationListener =
        LocationListener { location -> netLocation = location }

    @SuppressLint("MissingPermission")
    fun getLastLocation() : Location? {
        if ( !permissionManager.isLocationPermissionGranted() )
            return null

        val lastKnownLocationByGps =
            locationServiceManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val lastKnownLocationByNetwork =
            locationServiceManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if(lastKnownLocationByGps!=null && lastKnownLocationByNetwork!=null)
        {
            return if(lastKnownLocationByGps.accuracy>lastKnownLocationByNetwork.accuracy) {
                Log.i(TAG,"getLastLocation gps gives more accuracy")
                lastKnownLocationByGps
            } else {
                Log.i(TAG,"getLastLocation Network gives more accuracy")
                lastKnownLocationByNetwork
            }
        }
        return null
    }


    @SuppressLint("MissingPermission")
    fun registerForLocationUpdate(){
        if ( permissionManager.isLocationPermissionGranted() ) {
            if(locationServiceManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationServiceManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    gpsLocationListener
                )
            }
            if(locationServiceManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                locationServiceManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    0F,
                    networkLocationListener
                )
            }

        }
    }
}