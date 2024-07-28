package com.example.eventplanner

import android.app.Application
import android.content.Context.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import kotlin.math.sqrt

class SensorModel(application: Application) : AndroidViewModel(application), SensorEventListener {
    private var sensorManager: SensorManager = application.getSystemService(SENSOR_SERVICE) as SensorManager
    private var sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var acceleration = 10f
    private var currentAcceleration = SensorManager.GRAVITY_EARTH
    private var lastAcceleration = SensorManager.GRAVITY_EARTH
    private var lastSensorEvent: Long = 0
    var onShake: (() -> Unit)? = null

    init {
        buildSensor()
    }

    private fun buildSensor() {
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event != null) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Berechnung des Schüttelwerts wurde übernommen von:
            // https://www.geeksforgeeks.org/how-to-detect-shake-event-in-android/
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            // Log.d("Sensor event detected", "Event 1: $acceleration")

            // Acceleration über 12 wird als schütteln anerkannt
            if (acceleration > 12) {
                val currTime = System.currentTimeMillis()
                Log.d("Sensor event detected", "curr time: $currTime, last time: $lastSensorEvent")

                // verhindern vom Öffnen mehrerer Views durchs gleiche Event
                if (currTime - lastSensorEvent > 2000) {
                    lastSensorEvent = currTime
                    Log.d("Sensor event detected", "Event 2: $acceleration")
                    onShake?.invoke();
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

}