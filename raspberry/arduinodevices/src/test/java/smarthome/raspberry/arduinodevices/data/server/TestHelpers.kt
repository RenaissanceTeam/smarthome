package smarthome.raspberry.arduinodevices.data.server

import com.nhaarman.mockitokotlin2.mock
import smarthome.raspberry.arduinodevices.data.server.entity.Request
import smarthome.raspberry.arduinodevices.data.server.entity.RequestIdentifier

fun requestWith(identifier: RequestIdentifier, params: Map<String, String>): Request = Request(identifier, params)
fun requestWith(params: Map<String, String>): Request = Request(mock(), params)
fun requestWith(params: Map<String, String>, body: String): Request = Request(mock(), params, body)
fun requestWith(body: String): Request = Request(mock(), body = body)