package com.ftc16626.missioncontrol.webserver

import fi.iki.elonen.NanoHTTPD

data class RequestRESTResponse(
    val status: NanoHTTPD.Response.Status,
    val mimeType: String,
    val response: String
)