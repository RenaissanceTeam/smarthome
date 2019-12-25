package smarthome.raspberry.arduinodevices.data.server.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import org.koin.experimental.builder.singleBy
import smarthome.raspberry.arduinodevices.data.server.UdpServer
import smarthome.raspberry.arduinodevices.data.server.WebServerImpl
import smarthome.raspberry.arduinodevices.data.server.api.RequestHandler
import smarthome.raspberry.arduinodevices.data.server.api.WebServer
import smarthome.raspberry.arduinodevices.data.server.api.WebServerGate
import smarthome.raspberry.arduinodevices.data.server.httphandlers.AlertPost
import smarthome.raspberry.arduinodevices.data.server.httphandlers.InitPost
import smarthome.raspberry.arduinodevices.data.server.httphandlers.alert
import smarthome.raspberry.arduinodevices.data.server.httphandlers.init
import smarthome.raspberry.arduinodevices.data.server.mapper.*
import smarthome.raspberry.arduinodevices.data.server.nano.DelegatableNanoHttpd
import smarthome.raspberry.arduinodevices.data.server.nano.NanoHttpdToWebServerAdapter

val serverModule = module {
    singleBy<WebServer, WebServerImpl>()
    single { UdpServer() }
    
    factoryBy<WebServerGate, NanoHttpdToWebServerAdapter>()
    factory { DelegatableNanoHttpd() }
    factoryBy<NanoMethodToMethodMapper, NanoMethodToMethodMapperImpl>()
    factoryBy<JsonDeviceMapper, JsonDeviceMapperImpl>()
    
    factoryBy<HttpSessionToRequestMapper, HttpSessionToRequestMapperImpl>()
    factoryBy<ResponseCodeToStatusMapper, ResponseCodeToStatusMapperImpl>()
    factoryBy<ResponseToNanoResponseMapper, ResponseToNanoResponseMapperImpl>()
    
    factoryBy<RequestHandler, InitPost>(named(init))
    factoryBy<RequestHandler, AlertPost>(named(alert))
}