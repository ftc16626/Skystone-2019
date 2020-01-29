package com.ftc16626.missioncontrol

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Environment
import android.util.Log
import com.ftc16626.missioncontrol.math.imu.PositionIntegrator
import com.ftc16626.missioncontrol.math.Vector3
import com.ftc16626.missioncontrol.util.*
import com.ftc16626.missioncontrol.util.exceptions.DirectoryNotAccessibleException
import com.ftc16626.missioncontrol.util.exceptions.UnableToCreateDirectoryException
import com.ftc16626.missioncontrol.util.profiles.PilotProfileHandler
import com.ftc16626.missioncontrol.webserver.RequestRESTListener
import com.ftc16626.missioncontrol.webserver.RequestRESTResponse
import com.ftc16626.missioncontrol.webserver.WebServer
import com.ftc16626.missioncontrol.websocket.SocketListener
import com.ftc16626.missioncontrol.websocket.WebSocket
import fi.iki.elonen.NanoHTTPD
import org.java_websocket.handshake.ClientHandshake
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
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

    private val mainDirectory: File
    private val logDirectory: File
    private val profileDirectory: File

    private val scribe: Scribe
    private val positionIntegrator: PositionIntegrator

    val pilotProfileHandler: PilotProfileHandler

    private val liveVariable = mutableMapOf<String, Any>()

    init {
        webSocket.addSocketListener(this)

        val sdCardPath = Environment.getExternalStorageDirectory()
        val mainDirectoryPath = File("$sdCardPath/FIRST/MissionControl")

        mainDirectory = setupMainDirectory(mainDirectoryPath)
        logDirectory = setupLogDirectory(mainDirectory, "mc-logs")
        profileDirectory = setupLogDirectory(mainDirectoryPath, "profiles")

        scribe = Scribe()
        positionIntegrator = PositionIntegrator(
            Vector3(0.0, 0.0, 0.0),
            Vector3(0.0, 0.0, 0.0),
            Vector3(0.0, 0.0, 0.0)
        )

        pilotProfileHandler = PilotProfileHandler(profileDirectory)
        pilotProfileHandler.init()

        webServer.registerRESTRequest("logs", NanoHTTPD.Method.GET, object : RequestRESTListener {
            override fun onRequest(url: List<String>): RequestRESTResponse {
                val fileList = JSONArray()

                logDirectory.walk().forEach {
                    if (!it.isDirectory) fileList.put(it.name)
                }

                val jsonResponse = JSONObject().put("fileNames", fileList)

                return RequestRESTResponse(
                    NanoHTTPD.Response.Status.OK,
                    "application/json",
                    jsonResponse.toString()
                )
            }
        })

        webServer.registerRESTRequest("log", NanoHTTPD.Method.GET, object : RequestRESTListener {
            override fun onRequest(url: List<String>): RequestRESTResponse {
                val logFile = File(logDirectory, url[0])

                if (!logFile.exists()) {
                    return RequestRESTResponse(
                        NanoHTTPD.Response.Status.NOT_FOUND,
                        "text/plain",
                        "File not found"
                    )
                }

                return RequestRESTResponse(
                    NanoHTTPD.Response.Status.OK,
                    "text/plain",
                    logFile.readText()
                )
            }
        })

        webServer.registerRESTRequest(
            "pilot-profiles",
            NanoHTTPD.Method.GET,
            object : RequestRESTListener {
                override fun onRequest(url: List<String>): RequestRESTResponse {
                    val profileList = pilotProfileHandler.profileList
                    val profileListJSON = JSONArray()
                    profileList.forEachIndexed { index, element ->
                        val obj = JSONObject(element.toJSON())
                        obj.put("__pos__", index)
                        profileListJSON.put(obj)
                    }

                    return RequestRESTResponse(
                        NanoHTTPD.Response.Status.OK,
                        "application/json",
                        JSONObject().put("profiles", profileListJSON).put(
                            "currentPos",
                            pilotProfileHandler.currentProfilePos
                        ).toString()
                    )
                }
            })

        webServer.registerRESTRequest(
            "get-live-var",
            NanoHTTPD.Method.GET,
            object : RequestRESTListener {
                override fun onRequest(url: List<String>): RequestRESTResponse {
                    return RequestRESTResponse(
                        NanoHTTPD.Response.Status.OK,
                        "application/json",
                        JSONObject().put("value", getLiveVariable(url[0])).toString()
                    )
                }
            }
        )

        webServer.registerRESTRequest(
            "set-live-var",
            NanoHTTPD.Method.GET,
            object : RequestRESTListener {
                override fun onRequest(url: List<String>): RequestRESTResponse {
                    liveVariable[url[0]] = url[1]

                    return RequestRESTResponse(
                        NanoHTTPD.Response.Status.OK,
                        "application/json",
                        JSONObject().put("result", "success").toString()
                    )
                }
            }
        )
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

    fun logAndBroadcast(msg: String, tag: String) {
        logAndBroadcast(LogModel(msg, tag))
    }

    fun logAndBroadcast(msg: String, tag: String, time: Date) {
        logAndBroadcast(LogModel(msg, tag, time))
    }

    fun logAndBroadcast(model: LogModel) {
        log(model)
        broadcast(model)
    }

    fun log(msg: String, tag: String) {
        log(LogModel(msg, tag))
    }

    fun log(msg: String, tag: String, time: Date) {
        log(LogModel(msg, tag, time))
    }

    fun log(model: LogModel) {
        scribe.writeLine(model)
    }

    fun broadcast(msg: String, tag: String) {
        broadcast(LogModel(msg, tag))
    }

    fun broadcast(msg: String, tag: String, time: Date) {
        broadcast(LogModel(msg, tag, time))
    }

    fun broadcast(model: LogModel) {
        webSocket.broadcast(model)
    }

    fun sendInitPacket() {
        sendInitPacket(arrayOf())
    }

    fun sendInitPacket(sensorKeys: Array<String>) {
        val packet = JSONObject()

        val sensorJSON = JSONArray()
        sensorKeys.forEach {
            sensorJSON.put(it)
        }

        packet.put("sensor-keys", sensorJSON)


        webSocket.broadcast(LogModel(packet.toString(), "init", Date()))
    }

    fun startLogging() {
        this.sendLogs = true
//        this.turnOnSensorReading()
        this.broadcastLoggingStatus()

        val date = Date()
        val formatter = SimpleDateFormat("yyyy-M-dd HH:mm:ss.SSS")
        val stringDate = formatter.format(date)
        scribe.beginWrite(File(logDirectory, "log-$stringDate.txt"))
    }

    fun stopLogging() {
        this.sendLogs = false
//        this.turnOffSensorReading()
        this.broadcastLoggingStatus()

        scribe.closeWrite()
    }

    private fun turnOnSensorReading() {
        this.sendSensorData = true

        sensorManager?.registerListener(
            this,
            sensorAccelerometer,
            SensorManager.SENSOR_DELAY_FASTEST
        )
    }

    private fun turnOffSensorReading() {
        this.sendSensorData = false

        sensorManager?.unregisterListener(this)
    }

    override fun onOpen(conn: org.java_websocket.WebSocket, handshake: ClientHandshake) {
        sendInitPacket()
    }

    override fun onFormattedMessage(conn: org.java_websocket.WebSocket, msg: LogModel) {
        when (msg.tag) {
            "cmd" -> handleCommand(msg.msg)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_ACCELEROMETER) return

        val xLog = LogModel(
            event.values[0].toString(),
            "accelerometer-x",
            Date()
        )
        val yLog = LogModel(
            event.values[1].toString(),
            "accelerometer-y",
            Date()
        )
        val zLog = LogModel(
            event.values[2].toString(),
            "accelerometer-z",
            Date()
        )

        positionIntegrator.update(
            Vector3(
                event.values[0].toDouble(),
                event.values[1].toDouble(),
                event.values[2].toDouble()
            ), System.currentTimeMillis().toDouble() / 1000
        )

        val velXLog = LogModel(
            positionIntegrator.currentVel.x.toString(),
            "velocity-x",
            Date()
        )
        val velYLog = LogModel(
            positionIntegrator.currentVel.y.toString(),
            "velocity-y",
            Date()
        )
        val velZLog = LogModel(
            positionIntegrator.currentVel.z.toString(),
            "velocity-z",
            Date()
        )
        val posXLog = LogModel(
            positionIntegrator.currentPos.x.toString(),
            "position-x",
            Date()
        )
        val posYLog = LogModel(
            positionIntegrator.currentPos.y.toString(),
            "position-y",
            Date()
        )
        val posZLog = LogModel(
            positionIntegrator.currentPos.z.toString(),
            "position-z",
            Date()
        )

        webSocket.broadcast(xLog)
        webSocket.broadcast(yLog)
        webSocket.broadcast(zLog)

        scribe.writeLine(xLog)
        scribe.writeLine(yLog)
        scribe.writeLine(zLog)
        scribe.writeLine(velXLog)
        scribe.writeLine(velYLog)
        scribe.writeLine(velZLog)
        scribe.writeLine(posXLog)
        scribe.writeLine(posYLog)
        scribe.writeLine(posZLog)
    }

    private fun handleCommand(cmd: String) {
        Log.i(TAG, cmd)

        val cmdSplit = cmd.split(' ')
        when (cmdSplit[0]) {
            "logging-start" -> startLogging()
            "logging-stop" -> stopLogging()
        }

        if (cmdSplit[0] in commandList) {
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
                JSONArray()//.put("accelerometer-x").put("accelerometer-y").put("accelerometer-z")
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

    fun registerLiveVariable(key: String, value: Any) {
        liveVariable[key] = value
    }

    fun clearLiveVariables() {
        liveVariable.clear()
    }

    fun getLiveVariable(key: String): Any? {
        return liveVariable[key]
    }

    private fun setupMainDirectory(path: File): File {
        fun isExternalStorageWritable(): Boolean {
            return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        }

        fun isExternalStorageReadable(): Boolean {
            return Environment.getExternalStorageState() in
                    setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
        }

        if (!isExternalStorageWritable()) {
            val errorString = "MissionControl directory not readable/writable"
            Log.e(TAG, errorString)
            throw DirectoryNotAccessibleException(errorString)
        }

        if (!(path.exists() && path.isDirectory)) {
            Log.i(TAG, "Directory doesn't exist. Initializing...")
            if (!path.mkdirs()) {
                val errorString = "Unable to create MissionControl directory"
                Log.e(TAG, errorString)
                throw UnableToCreateDirectoryException(errorString)
            }
        }

        return path
    }

    private fun setupLogDirectory(parent: File, path: String): File {
        val path = File(parent, "/$path")

        if (!(path.exists() && path.isDirectory)) {
            Log.i(TAG, "Directory doesn't exist. Initializing...")
            if (!path.mkdirs()) {
                val errorString = "Unable to create MissionControl Log directory"
                Log.e(TAG, errorString)
                throw UnableToCreateDirectoryException(errorString)
            }
        }

        return path
    }
}

enum class SettingType {
    STRING, BOOL, INT, FLOAT, LONG, SHORT, BYTE, DOUBLE, ARRAY, NONE
}