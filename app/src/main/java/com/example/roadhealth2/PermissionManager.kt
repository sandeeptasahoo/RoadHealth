package com.example.roadhealth2

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionManager(private val activity: Activity) {

    private val context: Context =activity.applicationContext
    private val allPermissionList= arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION)

    fun askAllPermission() {
        val permissionNotGranted= arrayListOf<String>()
        for( permission in allPermissionList)
        {
            if(!isPermissionGranted(permission))
            {
                permissionNotGranted.add(permission)
            }
        }
        if(permissionNotGranted.isNotEmpty())
        {
            requestPermission(permissionNotGranted.toTypedArray())
        }
    }

    fun isPermissionGranted(permission : String): Boolean {
        return ActivityCompat.checkSelfPermission(context,permission)==PackageManager.PERMISSION_GRANTED

    }

    fun requestPermission(permissions:Array<String>) {
        ActivityCompat.requestPermissions(activity,permissions,1)
    }

    fun isLocationPermissionGranted():Boolean
    {
        return isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION) && isPermissionGranted(android.Manifest.permission.ACCESS_COARSE_LOCATION)
    }
}