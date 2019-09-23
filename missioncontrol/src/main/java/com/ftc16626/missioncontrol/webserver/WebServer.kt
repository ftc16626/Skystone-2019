package com.ftc16626.missioncontrol.webserver

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import java.io.IOException

class WebServer : NanoHTTPD(PORT) {
    companion object {
        const val PORT = 8888
        const val TAG = "MissionControlWebServer"
    }

    private val requestRESTList: MutableList<RequestREST> = mutableListOf()

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
        var response: Response? = null

        for(request in requestRESTList) {
            if(request.uri == uri && request.method == session.method) {
                val (status, mimeType, payload) = request.listener.onRequest()

                response = newFixedLengthResponse(status, mimeType, payload)
            }
        }

        if(response == null) {
            response =  when (uri) {
                "/" -> newFixedLengthResponse(
                    Response.Status.OK,
                    MIME_HTML,
                    "<html><body><h1>Mission Control Root</h1></body></html>"
                )
                "/base" -> newFixedLengthResponse(
                    Response.Status.OK,
                    MIME_HTML,
                    "<html><body><h1>Mission Control Base uri</h1></body></html>"
                )
                else -> newFixedLengthResponse(
                    Response.Status.OK,
                    MIME_HTML,
                    "<html><body><h1>Mission Control</h1></body></html>\""
                )
            }
        }

        response?.addHeader("Access-Control-Allow-Methods", "DELETE, GET, POST, PUT")
        response?.addHeader("Access-Control-Allow-Origin",  "*")
        response?.addHeader("Access-Control-Allow-Headers", "X-Requested-With")

        return response!!
    }

    fun registerRESTRequest(
        uri: String,
        method: NanoHTTPD.Method,
        listener: RequestRESTListener
    ) {
        requestRESTList.add(RequestREST(uri, method, listener))
    }

}