package com.ftc16626.missioncontrol.webserver

interface RequestRESTListener {
    fun onRequest(url: List<String>): RequestRESTResponse
}