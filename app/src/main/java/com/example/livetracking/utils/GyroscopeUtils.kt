package com.example.livetracking.utils

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.livetracking.domain.model.GyroData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

interface GyroscopeClient {
    fun sensorChanged(): Flow<GyroData>
    class GyroException(message: String) : Exception(message)
}


class DefaultGyroClient(
    val context: Context
) : GyroscopeClient {

    private val sensorManager: SensorManager =
        context.getSystemService(Activity.SENSOR_SERVICE) as SensorManager

    private val rotationSensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private var lastAccuracy: Int = SensorManager.SENSOR_STATUS_UNRELIABLE


    override fun sensorChanged(): Flow<GyroData> = callbackFlow {
        fun setGyroData(data: GyroData?) {
            data?.let {
                launch { send(it) }
            }
        }

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(p0: SensorEvent?) {
                if (lastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
                    throw GyroscopeClient.GyroException("Sensor UNRELIABLE")
                }

                if (p0?.sensor == rotationSensor) {
                    val rotationMatrix = FloatArray(9)
                    val orientation = FloatArray(3)

                    SensorManager.getRotationMatrixFromVector(rotationMatrix, p0?.values)
                    SensorManager.remapCoordinateSystem(
                        rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        rotationMatrix
                    )
                    SensorManager.getOrientation(rotationMatrix, orientation)

                    val pitch = Math.toDegrees(orientation[1].toDouble()).toFloat()
                    val roll = Math.toDegrees(orientation[2].toDouble()).toFloat()
                    val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()

                    setGyroData(
                        GyroData(
                            azimuth, roll, pitch
                        )
                    )
                }
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
                lastAccuracy = p1
            }

        }

        fun sensorStartListening() {
            sensorManager.registerListener(
                sensorEventListener,
                rotationSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        fun sensorStopListening() {
            sensorManager.unregisterListener(sensorEventListener)
        }

        sensorStartListening()
        awaitClose { sensorStopListening() }
    }

}

class GyroscopeUtils(val context: Context) : SensorEventListener,
    LiveData<GyroData>() {

    private val sensorManager: SensorManager =
        context.getSystemService(Activity.SENSOR_SERVICE) as SensorManager

    private val rotationSensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    private var lastAccuracy: Int = SensorManager.SENSOR_STATUS_UNRELIABLE

    private fun startListening() {
        sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onInactive() {
        super.onInactive()
        stopListening()
    }

    override fun onActive() {
        super.onActive()
        startListening()
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onSensorChanged(p0: SensorEvent?) {
        if (lastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return
        }

        if (p0?.sensor == rotationSensor) {
            val rotationMatrix = FloatArray(9)
            val orientation = FloatArray(3)

            SensorManager.getRotationMatrixFromVector(rotationMatrix, p0?.values)
            SensorManager.remapCoordinateSystem(
                rotationMatrix,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                rotationMatrix
            )
            SensorManager.getOrientation(rotationMatrix, orientation)

            val pitch = Math.toDegrees(orientation[1].toDouble()).toFloat()
            val roll = Math.toDegrees(orientation[2].toDouble()).toFloat()
            val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()

            value = GyroData(
                azimuth, roll, pitch
            )
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        lastAccuracy = p1
    }


}