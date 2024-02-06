package com.example.roadhealth2

object AnalysisData {
    var sensorFrameRate:Int=200000
    var ChangeOffset:Int=1
    var forceFilter:Float=0.5f

    val axial_sensor_x="Axial sensor X"
    val axial_sensor_y="Axial sensor Y"
    val axial_sensor_z="Axial sensor Z"

    val axial_change_x="Axial sensor X changed by"
    val axial_change_y="Axial sensor Y changed by"
    val axial_change_z="Axial sensor Z changed by"

    val growthIndicator_X="Growth Direction for X"
    val growthIndicator_Y="Growth Direction for Y"
    val growthIndicator_Z="Growth Direction for Z"

    val axial_change="Axial force magnitude"

    val direction="Direction"
    val directionChange="Direction change by"

    val locationChangedGps="Location changed through GPS"
    val locationChangedNetwork="Location changed through Network"

    val LastLocation="Last location"
}