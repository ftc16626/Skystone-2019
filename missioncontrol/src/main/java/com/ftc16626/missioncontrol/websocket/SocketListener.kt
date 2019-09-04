package com.ftc16626.missioncontrol.websocket

import com.ftc16626.missioncontrol.LogModel
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import java.nio.ByteBuffer

interface SocketListener {
    fun onMessage(conn: WebSocket, msg: String?) {}
    fun onFormattedMessage(conn: WebSocket, msg: LogModel) {}
    fun onSend(conn: WebSocket, payload: String) {}
    fun onSend(conn: WebSocket, payload: ByteArray) {}
    fun onSend(conn: WebSocket, payload: ByteBuffer) {}
    fun onOpen(conn: WebSocket, handshake: ClientHandshake) {}
    fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {}
    fun onStart() {}
    fun onError(conn: WebSocket?, e: Exception) {}
}