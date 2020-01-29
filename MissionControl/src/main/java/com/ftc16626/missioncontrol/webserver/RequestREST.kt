package com.ftc16626.missioncontrol.webserver

import fi.iki.elonen.NanoHTTPD

data class RequestREST(val uri: String, val method: NanoHTTPD.Method, val listener: RequestRESTListener)