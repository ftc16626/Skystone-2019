package com.ftc16266.missioncontrol.websocket

import android.util.Log
import com.google.gson.Gson
import com.ftc16266.missioncontrol.LogModel
import com.google.gson.JsonSyntaxException
import org.java_websocket.WebSocket
import org.java_websocket.exceptions.WebsocketNotConnectedException
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.util.*

class WebSocket : WebSocketServer(InetSocketAddress(PORT)) {
    companion object {
        const val PORT = 8889
        const val TAG = "MissionControlWebSocket"
    }

    private val socketListenerList = ArrayList<SocketListener>()
    private val connectionList = ArrayList<WebSocket>()

    fun addSocketListener(listener: SocketListener) {
        socketListenerList.add(listener)
    }

    // Since Java/Kotlin doesn't allow for multiple types
    fun sendRaw(conn: WebSocket, payload: String) {
        try {
            conn.send(payload)
        } catch (e: WebsocketNotConnectedException) {

        }

        for (listener in socketListenerList) {
            listener.onSend(conn, payload)
        }
    }

    fun sendRaw(conn: WebSocket, payload: ByteArray) {
        conn.send(payload)

        for (listener in socketListenerList) {
            listener.onSend(conn, payload)
        }
    }

    fun sendRaw(conn: WebSocket, payload: ByteBuffer) {
        conn.send(payload)

        for (listener in socketListenerList) {
            listener.onSend(conn, payload)
        }
    }

    fun sendMessage(conn: WebSocket, log: LogModel) {
        val gson = Gson()
        val payload = gson.toJson(log)
        sendRaw(conn, payload)
    }

    @JvmOverloads
    fun sendMessage(conn: WebSocket, msg: String, tag: String, time: Date = Date()) {
        sendMessage(conn, LogModel(msg, tag, time))
    }

    fun broadcast(log: LogModel) {
        for (conn in connectionList) {
            sendMessage(conn, log)
        }
    }

    @JvmOverloads
    fun broadcast(msg: String, tag: String, time: Date = Date()) {
        broadcast(LogModel(msg, tag, time))
    }

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        Log.i(TAG, "$conn has joined the room")

        for (listener in socketListenerList) {
            listener.onOpen(conn!!, handshake!!)
        }

        connectionList.add(conn!!)
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        Log.i(TAG, "$conn has left the room")

        for (listener in socketListenerList) {
            listener.onClose(conn!!, code, reason!!, remote)
        }

        connectionList.remove(conn!!)
    }

    override fun onMessage(conn: WebSocket?, msg: String?) {
        for (listener in socketListenerList) {
            listener.onMessage(conn!!, msg)
        }

        val gson = Gson()
        try {
            val log = gson.fromJson(msg!!, LogModel::class.java)

            for (listener in socketListenerList) {
                listener.onFormattedMessage(conn!!, log)
            }
        } catch (e: JsonSyntaxException) {
            Log.i(TAG, "Message not formatted using JSON")
        } catch (e: Exception) {
            Log.e(TAG, "$e")
        }

    }

    override fun onStart() {
        Log.i(TAG, "Mission Control socket server starting")
        // examples in github repo include this but apparently the function doesn't exist
        // setConnectionLostTimeout(0)
        // setConnectionLostTimeout(100

        for (listener in socketListenerList) {
            listener.onStart()
        }
    }

    override fun onError(conn: WebSocket?, e: Exception?) {
        Log.e(TAG, e.toString())
        e!!.printStackTrace()

        for (listener in socketListenerList) {
            listener.onError(conn!!, e)
        }
    }
}