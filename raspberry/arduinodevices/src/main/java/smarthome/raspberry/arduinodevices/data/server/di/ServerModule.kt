package smarthome.raspberry.arduinodevices.data.server.di

import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.raspberry.arduinodevices.data.server.mapper.HttpSessionToRequestIdentifierMapper
import smarthome.raspberry.arduinodevices.data.server.mapper.HttpSessionToRequestIdentifierMapperImpl

val serverModule = module {
    factoryBy<HttpSessionToRequestIdentifierMapper, HttpSessionToRequestIdentifierMapperImpl>()
}