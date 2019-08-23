package com.ftc16626.missioncontrol

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.ftc16626.missioncontrol.util.CommandListener
import com.ftc16626.missioncontrol.webserver.WebServer
import com.ftc16626.missioncontrol.websocket.SocketListener
import com.ftc16626.missioncontrol.websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class MissionControl(private val activity: Activity) : SocketListener,
    SensorEventListener {
    companion object {
        const val TAG = "MissionControl"
    }

    private val webServer = WebServer()
    private val webSocket = WebSocket()

    private var sendSensorData = false
    private var sendLogs = false

    private var sensorManager: SensorManager? = null
    private var sensorAccelerometer: Sensor? = null

    private val commandList = HashMap<String, CommandListener>()
    private val settingsTypeList = HashMap<String, SettingType>()
    private val settingsValueList = HashMap<String, Any>()

    init {
        webSocket.addSocketListener(this)
    }

    fun start() {
        Log.i(TAG, "Mission Control starting")
        webServer.start()
        webSocket.start()

        val localSensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager = localSensorManager
        sensorAccelerometer = localSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sendLogs = true
        if (sendSensorData) turnOnSensorReading()
    }

    fun stop() {
        sendLogs = false

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
        webSocket.sendMessage(conn, LogModel(getInitPacket(), "init", Date()))
    }

//    override fun onMessage(conn: org.java_websocket.WebSocket, msg: String?) {
//        Log.i("MissionControl", "Message: $msg")
//    }

    override fun onFormattedMessage(conn: org.java_websocket.WebSocket, msg: LogModel) {
        when (msg.tag) {
            "cmd" -> handleCommand(msg.msg)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_ACCELEROMETER) return

        webSocket.broadcast(
            LogModel(
                event.values[0].toString(),
                "accelerometer-x",
                Date()
            )
        )
        webSocket.broadcast(
            LogModel(
                event.values[1].toString(),
                "accelerometer-y",
                Date()
            )
        )
        webSocket.broadcast(
            LogModel(
                event.values[2].toString(),
                "accelerometer-z",
                Date()
            )
        )
    }

    private fun handleCommand(cmd: String) {
        Log.i(TAG, cmd)

        val cmdSplit = cmd.split(' ')
        when (cmdSplit[0]) {
            "logging-start" -> {
                this.sendLogs = true
                this.turnOnSensorReading()
                this.broadcastLoggingStatus()
            }

            "logging-stop" -> {
                this.sendLogs = false
                this.turnOffSensorReading()
                this.broadcastLoggingStatus()
            }
        }

        if (cmdSplit[0] in commandList) {
            val x = commandList[cmdSplit[0]]

            commandList[cmdSplit[0]]?.onCommand(
                cmdSplit.slice(
                    IntRange(
                        1,
                        cmdSplit.size - 1
                    )
                ).joinToString(" ")
            )
        }
    }

    private fun broadcastLoggingStatus() {
        webSocket.broadcast(
            LogModel(
                "logging ${if (this.sendLogs) "on" else "off"}",
                "status",
                Date()
            )
        )
    }

    private fun getInitPacket(): String {
        val packet = JSONObject()
            .put(
                "sensor-keys",
                JSONArray().put("accelerometer-x").put("accelerometer-y").put("accelerometer-z")
            )

        return packet.toString()
    }

    fun registerCommand(cmd: String, listener: CommandListener) {
        if (cmd in commandList)
            throw Exception("Command already exists")
        else
            commandList[cmd] = listener
    }

    // Settings with default values
    fun registerSettings(settings: Map<String, Any>) {
        for ((key, value) in settings) {
            settingsValueList[key] = value

            val valueType = when (value) {
                is String -> SettingType.STRING
                is Boolean -> SettingType.BOOL

                is Int -> SettingType.INT
                is Float -> SettingType.FLOAT
                is Double -> SettingType.DOUBLE
                is Long -> SettingType.LONG
                is Short -> SettingType.SHORT
                is Byte -> SettingType.BYTE
                is Array<*> -> SettingType.ARRAY

                else -> SettingType.NONE
            }

            settingsTypeList[key] = valueType
        }
    }
}

enum class SettingType {
    STRING, BOOL, INT, FLOAT, LONG, SHORT, BYTE, DOUBLE, ARRAY, NONE
}