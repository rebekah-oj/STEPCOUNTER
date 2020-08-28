package me.tap.stepcount

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        loadData()
        resetSteps()
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            showToast("No sensor detected on this device", Toast.LENGTH_SHORT)
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            if (event != null) {
                totalSteps = event.values[0]
                val currentSteps: Int = totalSteps.toInt() - previousTotalSteps.toInt()
                steps_taken.text = ("$currentSteps")

                circularProgressBar.apply {
                    setProgressWithAnimation(currentSteps.toFloat())
                }
            }
        }
    }

    private fun resetSteps() {
        steps_taken.setOnClickListener {
            showToast("Long tap to reset steps", Toast.LENGTH_SHORT)
        }

        steps_taken.setOnLongClickListener {
            previousTotalSteps = totalSteps
            steps_taken.text = 0.toString()
            saveData()

            true
        }
    }


    private fun saveData() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit()?.putFloat("key1", previousTotalSteps)
            ?.apply()
    }

    private fun loadData() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedNumber: Float = sharedPreferences.getFloat("key", 0f)
        Log.d("MainActivity", "$savedNumber")
        previousTotalSteps = savedNumber
    }
}