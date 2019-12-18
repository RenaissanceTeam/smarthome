package smarthome.raspberry.arduinodevices.data.server.di

import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.raspberry.arduinodevices.data.server.WebServerImpl
import smarthome.raspberry.arduinodevices.data.server.api.WebServer
import smarthome.raspberry.arduinodevices.data.server.api.WebServerGate
import smarthome.raspberry.arduinodevices.data.server.mapper.*
import smarthome.raspberry.arduinodevices.data.server.nano.NanoHttpdToWebServerAdapter

val serverModule = module {
    factoryBy<HttpSessionToRequestMapper, HttpSessionToRequestMapperImpl>()
    factoryBy<ResponseCodeToStatusMapper, ResponseCodeToStatusMapperImpl>()
    factoryBy<ResponseToNanoResponseMapper, ResponseToNanoResponseMapperImpl>()
    factoryBy<WebServer, WebServerImpl>()
    factoryBy<WebServerGate, NanoHttpdToWebServerAdapter>()
    factoryBy<NanoMethodToMethodMapper, NanoMethodToMethodMapperImpl>()
    factoryBy<JsonDeviceMapper, JsonDeviceMapperImpl>()
}