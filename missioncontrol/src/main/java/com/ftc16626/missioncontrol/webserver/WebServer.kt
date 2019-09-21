package com.ftc16626.missioncontrol.webserver

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import java.io.IOException

class WebServer : NanoHTTPD(PORT) {
    companion object {
        const val PORT = 8888
        const val TAG = "MissionControlWebServer"
    }

    override fun start() {
        try {
            start(SOCKET_READ_TIMEOUT, false)
            Log.i(TAG, "Starting MissionControl web server")
        } catch (e: IOException) {
            Log.i(TAG, "Could not start MissionControl: $e")
        }
    }

    override fun serve(session: IHTTPSession?): Response {
        val uri: String = session!!.uri

        if (uri == "/") {
            return newFixedLengthResponse("<html><body><h1>Mission Control Root</h1></body></html>")
        } else if (uri == "/base") {
            return newFixedLengthResponse("<html><body><h1>Mission Control Base uri</h1></body></html>")
        }

        val msg = "<html><body><h1>Mission Control</h1></body></html>"

        return newFixedLengthResponse(msg)
    }
}