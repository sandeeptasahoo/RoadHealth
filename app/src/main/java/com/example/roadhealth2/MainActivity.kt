package com.example.roadhealth2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roadhealth2.UiComponent.RecyclerViewAdapter

class MainActivity : AppCompatActivity() {

    private val adapter = RecyclerViewAdapter()
    private lateinit var recyclerView: RecyclerView
    private lateinit var sensorUtilManager:SensorUtilManager
    private lateinit var permissionManager: PermissionManager
    private lateinit var locationManagerImpl: LocationManagerImpl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView=findViewById(R.id.RecycleViewer)

        setRecyclerView()
        permissionManager=PermissionManager(this)
        permissionManager.askAllPermission()
        locationManagerImpl=LocationManagerImpl(this,permissionManager)
        locationManagerImpl.registerForLocationUpdate()
        sensorUtilManager=SensorUtilManager(this,adapter,locationManagerImpl)

    }

    fun setRecyclerView()
    {
        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter=adapter
    }

    override fun onResume() {
        super.onResume()
        sensorUtilManager.registerAxialSensor()
    }

    override fun onPause() {
        super.onPause()
        sensorUtilManager.unregisterAxialSensor()
    }
}