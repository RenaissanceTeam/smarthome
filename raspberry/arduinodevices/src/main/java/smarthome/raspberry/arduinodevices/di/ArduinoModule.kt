package smarthome.raspberry.arduinodevices.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.library.common.DeviceChannel
import smarthome.raspberry.arduinodevices.data.mapper.ControllerStateToValuePayloadMapper
import smarthome.raspberry.arduinodevices.data.mapper.ControllerStateToValuePayloadMapperImpl
import smarthome.raspberry.arduinodevices.data.mapper.ValuePayloadToControllerStateMapper
import smarthome.raspberry.arduinodevices.data.mapper.ValuePayloadToControllerStateMapperImpl
import smarthome.raspberry.arduinodevices.data.server.di.serverModule
import smarthome.raspberry.arduinodevices.domain.ArduinoDeviceChannel
import smarthome.raspberry.channel.api.domain.arduinoChannel


private val domain = module {
    single<DeviceChannel>(named(arduinoChannel)) {
        ArduinoDeviceChannel(
            httpServer = get(),
            udpServer = get(),
            observeHomeLifecycleUseCase = get(),
            arduinoApiGson = get(named(arduinoApiGson)),
            stateToValueMapper = get(),
            valueToStateMapper = get()
    ) }
}

private val data = module {
    factoryBy<ValuePayloadToControllerStateMapper, ValuePayloadToControllerStateMapperImpl>()
    factoryBy<ControllerStateToValuePayloadMapper, ControllerStateToValuePayloadMapperImpl>()
    single<Gson>(named(arduinoApiGson)) {
        GsonBuilder()
            .setLenient()
            .create()
    }
}


val arduinoModule = listOf(
    domain,
    data,
    serverModule
)

private const val deviceGson = "deviceGson"
private const val arduinoApiGson = "arduinoApiGson"