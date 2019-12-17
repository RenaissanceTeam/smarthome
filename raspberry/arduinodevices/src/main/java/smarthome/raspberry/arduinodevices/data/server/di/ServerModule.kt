package smarthome.raspberry.arduinodevices.data.server.di

import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.raspberry.arduinodevices.data.server.nano.NanoHttpdToWebServerAdapter
import smarthome.raspberry.arduinodevices.data.server.api.WebServer
import smarthome.raspberry.arduinodevices.data.server.api.WebServerGate
import smarthome.raspberry.arduinodevices.data.server.WebServerImpl
import smarthome.raspberry.arduinodevices.data.server.mapper.*

val serverModule = module {
    factoryBy<HttpSessionToRequestIdentifierMapper, HttpSessionToRequestIdentifierMapperImpl>()
    factoryBy<ResponseCodeToStatusMapper, ResponseCodeToStatusMapperImpl>()
    factoryBy<ResponseToNanoResponseMapper, ResponseToNanoResponseMapperImpl>()
    factoryBy<WebServer, WebServerImpl>()
    factoryBy<WebServerGate, NanoHttpdToWebServerAdapter>()
    factoryBy<NanoMethodToMethodMapper, NanoMethodToMethodMapperImpl>()
    factoryBy<ValuePayloadToControllerStateMapper, ValuePayloadToControllerStateMapperImpl>()
}