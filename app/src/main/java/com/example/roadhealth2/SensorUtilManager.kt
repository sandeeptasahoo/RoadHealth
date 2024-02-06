package com.example.roadhealth2

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.util.Log
import com.example.roadhealth2.UiComponent.RecyclerViewAdapter
import kotlin.math.absoluteValue
import kotlin.math.pow


class SensorUtilManager(context: Context, sensorDataAdapter: RecyclerViewAdapter,val locationManagerImpl: LocationManagerImpl) {

    val data = mutableListOf<DataPacket>()
    var previousSensorEvent: MutableMap<String,Float> = mutableMapOf()
    private val gravity = mutableListOf<Float>()
    var dataMap: MutableMap<String, Float> = mutableMapOf()
    var dataMap2: MutableMap<String, Float> = mutableMapOf()




    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val axialSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val axialSensorListener: SensorEventListener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent?) {
            if (event != null) {
                val eventCopy = removeGravity(event)
                //registerChangeInSensor(eventCopy)
                //detectDirection(eventCopy)
                registerAxialGrowth(eventCopy)
                //deepCopy(event)
                updateData()
                sensorDataAdapter.updateRecyclerView(data)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }

    private fun deepCopy(event:SensorEvent)
    {
        previousSensorEvent[AnalysisData.axial_sensor_x] = event.values[0]
        previousSensorEvent[AnalysisData.axial_sensor_y] = event.values[1]
        previousSensorEvent[AnalysisData.axial_sensor_z] = event.values[2]
    }

    private fun registerAxialGrowth(event: SensorEvent) {

        if (dataMap2.containsKey(AnalysisData.axial_sensor_x)) {
            val diffx = event.values[0]- dataMap2[AnalysisData.axial_sensor_x]!!
            val diffy = event.values[1]- dataMap2[AnalysisData.axial_sensor_y]!!
            val diffz = event.values[2]- dataMap2[AnalysisData.axial_sensor_z]!!


            if(dataMap2.containsKey(AnalysisData.axial_change_x))
            {
                dataMap2[AnalysisData.growthIndicator_X] =
                    diffx * dataMap2[AnalysisData.axial_change_x]!!
                dataMap2[AnalysisData.growthIndicator_Y] =
                    diffy * dataMap2[AnalysisData.axial_change_y]!!
                dataMap2[AnalysisData.growthIndicator_Z] =
                    diffz * dataMap2[AnalysisData.axial_change_z]!!

                if (dataMap2[AnalysisData.growthIndicator_X]!! < 0.0) {
                    dataMap[AnalysisData.axial_change_x] =
                        dataMap2[AnalysisData.axial_sensor_x]!! - dataMap[AnalysisData.axial_sensor_x]!!
                    dataMap[AnalysisData.axial_sensor_x] = dataMap2[AnalysisData.axial_sensor_x]!!
                    Log.i("SensorUtilManager","${AnalysisData.axial_sensor_x} = ${dataMap[AnalysisData.axial_sensor_x]} ")
                    Log.i("SensorUtilManager","${AnalysisData.axial_change_x} = ${dataMap[AnalysisData.axial_change_x]} ")
                }
                if (dataMap2[AnalysisData.growthIndicator_Y]!! < 0.0) {
                    dataMap[AnalysisData.axial_change_y] =
                        dataMap2[AnalysisData.axial_sensor_y]!! - dataMap[AnalysisData.axial_sensor_y]!!
                    dataMap[AnalysisData.axial_sensor_y] = dataMap2[AnalysisData.axial_sensor_y]!!

                    Log.i("SensorUtilManager","${AnalysisData.axial_sensor_y} = ${dataMap[AnalysisData.axial_sensor_y]} ")
                    Log.i("SensorUtilManager","${AnalysisData.axial_change_y} = ${dataMap[AnalysisData.axial_change_y]} ")
                }
                if (dataMap2[AnalysisData.growthIndicator_Z]!! < 0.0) {
                    dataMap[AnalysisData.axial_change_z] =
                        dataMap2[AnalysisData.axial_sensor_z]!! - dataMap[AnalysisData.axial_sensor_z]!!
                    dataMap[AnalysisData.axial_sensor_z] = dataMap2[AnalysisData.axial_sensor_z]!!

                    Log.i("SensorUtilManager","${AnalysisData.axial_sensor_z} = ${dataMap[AnalysisData.axial_sensor_z]} ")
                    Log.i("SensorUtilManager","${AnalysisData.axial_change_z} = ${dataMap[AnalysisData.axial_change_z]} ")
                }
            }


            dataMap2[AnalysisData.axial_change_x] = diffx
            dataMap2[AnalysisData.axial_change_y] = diffy
            dataMap2[AnalysisData.axial_change_z] = diffz


        }
        dataMap2[AnalysisData.axial_sensor_x] = event.values[0]
        dataMap2[AnalysisData.axial_sensor_y] = event.values[1]
        dataMap2[AnalysisData.axial_sensor_z] = event.values[2]

    }

    private fun removeGravity(event: SensorEvent): SensorEvent {
        val alpha: Float = 0.8f

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0]
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1]
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2]

        event.values[0] = event.values[0] - gravity[0]
        event.values[1] = event.values[1] - gravity[1]
        event.values[2] = event.values[2] - gravity[2]
        return event
    }

    init {
        dataMap[AnalysisData.directionChange] = 0.0f


        dataMap[AnalysisData.axial_change_x] = 0.0f
        dataMap[AnalysisData.axial_change_y] = 0.0f
        dataMap[AnalysisData.axial_change_z] = 0.0f

        dataMap[AnalysisData.axial_sensor_x] = 0.0f
        dataMap[AnalysisData.axial_sensor_y] = 0.0f
        dataMap[AnalysisData.axial_sensor_z] = 0.0f

        dataMap[AnalysisData.axial_change] = 0.0f
        dataMap[AnalysisData.direction] = 0.0f



        gravity.add(0.0f)
        gravity.add(0.0f)
        gravity.add(0.0f)

    }

    /* private fun registerChangeInSensor(event: SensorEvent) {
         if(previousSensorEvent==null)
         {
             return
         }


         val diffX:Float= previousSensorEvent!!.values[0].minus(event.values[0])
         val diffY:Float= previousSensorEvent!!.values[1].minus(event.values[1])
         val diffZ:Float= previousSensorEvent!!.values[2].minus(event.values[2])

         val mag = (diffX.pow(2)+diffY.pow(2)+diffZ.pow(2))
         dataMap[AnalysisData.axial_change]=mag.toString()
         if(mag>AnalysisData.ChangeOffset)
         {

         }
         if(diffX>=AnalysisData.ChangeOffset )
         {
             dataMap[AnalysisData.axial_change_x]=formatDecimal(diffX,1)
         }
         if(diffY>=AnalysisData.ChangeOffset)
         {
             dataMap[AnalysisData.axial_change_y]=formatDecimal(diffY,1)
         }
         if(diffZ>=AnalysisData.ChangeOffset)
         {
             dataMap[AnalysisData.axial_change_z]=formatDecimal(diffZ,1)
         }

     }

     */

    /* private fun detectDirection(event:SensorEvent)
     {
         var mag=event.values[0].pow(2)+event.values[1].pow(2)+event.values[2].pow(2)
         mag=mag.pow(0.5f)
         val currentDirection= mutableListOf<Float>()
         for(i in 0..2)
         {
             currentDirection.add(event.values[i]/mag)
         }

         val previousDirection: List<Float>? =dataMap[AnalysisData.direction]?.split("+ ")?.map{
             it.split(":")[1].toFloat()
         }
         val changeInDirection= mutableListOf<Float>()
         if (previousDirection != null) {
             for (i in 0..2) {
                 changeInDirection.add(previousDirection[i].minus(currentDirection[i])*10)
             }
             dataMap[AnalysisData.directionChange]="x:${formatDecimal(changeInDirection[0],1)}+ y:${formatDecimal(changeInDirection[1],1)}+ z:${formatDecimal(changeInDirection[2],1)}"
         }
         dataMap[AnalysisData.direction]="x:${formatDecimal(currentDirection[0],1)}+ y:${formatDecimal(currentDirection[1],1)}+ z:${formatDecimal(currentDirection[2],1)}"

     }

     */

    private fun updateData() {
        data.clear()
        /*for (element in dataMap) {
            data.add(DataPacket(element.key,formatDecimal(element.value,1)))
            //Log.i("SensorUtilManager", "${element.key} : ${formatDecimal(element.value, 1)}")
        }
         */
        addLocationToView()


    }

    fun addLocationToView()
    {
        data.add(DataPacket(AnalysisData.locationChangedGps,locationManagerImpl.gpsLocation?.latitude.toString() + "  "+locationManagerImpl.gpsLocation?.longitude.toString()))
        data.add(DataPacket(AnalysisData.locationChangedNetwork,locationManagerImpl.netLocation?.latitude.toString() + "  "+locationManagerImpl.netLocation?.longitude.toString()))
//        locationManagerImpl.getLastLocation()?.let{
//            data.add(DataPacket(AnalysisData.LastLocation,it.latitude.toString() + "  "+it.longitude.toString()))
//        }
    }

    fun registerAxialSensor() {
        sensorManager.registerListener(
            axialSensorListener,
            axialSensor,
            AnalysisData.sensorFrameRate
        )
    }

    fun unregisterAxialSensor() {
        sensorManager.unregisterListener(axialSensorListener, axialSensor)
    }

    fun formatDecimal(num: Float, decimalPlace: Int): String {
        val d = 10.0.pow(decimalPlace.toDouble())
        val ans = (num * d).toInt() / d
        return ans.toString()
    }

}