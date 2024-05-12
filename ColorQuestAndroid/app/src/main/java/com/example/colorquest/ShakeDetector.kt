package com.example.colorquest

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(private val context: Context) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f
    private var lastTime: Long = 0

    private var shakeListener: OnShakeListener? = null

    private val shakeThreshold = 5000 // Adjust this value as needed

    interface OnShakeListener {
        fun onShake()
    }

    fun setOnShakeListener(listener: OnShakeListener) {
        shakeListener = listener
    }

    fun start() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stop() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            val timeDifference = currentTime - lastTime

            if (timeDifference > 100) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val deltaX = x - lastX
                val deltaY = y - lastY
                val deltaZ = z - lastZ

                val acceleration = sqrt((deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ).toDouble()) / timeDifference * 10000

                if (acceleration > shakeThreshold) {
                    shakeListener?.onShake()
                }

                lastX = x
                lastY = y
                lastZ = z
                lastTime = currentTime
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }
}
