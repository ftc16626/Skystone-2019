package com.ftc16266.missioncontrol

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.ftc16266.missioncontrol.webserver.WebServer
import com.ftc16266.missioncontrol.websocket.SocketListener
import com.ftc16266.missioncontrol.websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import java.util.*

class MissionControl(activity: Activity, sendSensorData: Boolean) : SocketListener,
    SensorEventListener {
    companion object {
        const val TAG = "MissionControl"
    }

    private val activity = activity

    private val webServer = WebServer()
    private val webSocket = WebSocket()

    private var sendSensorData = sendSensorData


    private var sensorManager: SensorManager? = null
    private var sensorAccelerometer: Sensor? = null


    init {
        webSocket.addSocketListener(this)
    }


    fun start() {
        Log.i(TAG, "Mission Control starting")
        webServer.start()
        webSocket.start()

        //        sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val localSensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager = localSensorManager
        sensorAccelerometer = localSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (sendSensorData) turnOnSensorReading()
    }

    fun stop() {
        webServer.stop()
        webSocket.stop()
    }

    private fun turnOnSensorReading() {
        this.sendSensorData = true

        sensorManager?.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    private fun turnOffSensorReading() {
        this.sendSensorData = false

        sensorManager?.unregisterListener(this)
    }

    override fun onOpen(conn: org.java_websocket.WebSocket, handshake: ClientHandshake) {
        webSocket.sendMessage(conn, LogModel("testing ", "init", Date()))
    }

    override fun onMessage(conn: org.java_websocket.WebSocket?, msg: String?) {
        Log.i("MissionControl", "Message: $msg")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_ACCELEROMETER) return

//        Log.i(TAG, "" + event.values[0] + ", " + event.values[1])
//        webSocket.sendMessage()
        webSocket.broadcast(
            LogModel(
                "${event.values[0]}, ${event.values[1]}",
                "accelerometer",
                Date()
            )
        )
    }
}