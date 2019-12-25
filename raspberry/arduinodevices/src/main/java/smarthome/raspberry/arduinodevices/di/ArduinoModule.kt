package smarthome.raspberry.arduinodevices.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.experimental.builder.factoryBy
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.library.common.DeviceChannel
import smarthome.library.common.IotDevice
import smarthome.library.common.constants.typeField
import smarthome.library.common.util.RuntimeTypeAdapterFactory
import smarthome.raspberry.arduinodevices.data.mapper.ControllerStateToValuePayloadMapper
import smarthome.raspberry.arduinodevices.data.mapper.ControllerStateToValuePayloadMapperImpl
import smarthome.raspberry.arduinodevices.data.mapper.ValuePayloadToControllerStateMapper
import smarthome.raspberry.arduinodevices.data.mapper.ValuePayloadToControllerStateMapperImpl
import smarthome.raspberry.arduinodevices.data.server.di.serverModule
import smarthome.raspberry.arduinodevices.domain.ArduinoDevice
import smarthome.raspberry.arduinodevices.domain.ArduinoDeviceChannel
import smarthome.raspberry.arduinodevices.domain.controllers.ArduinoAnalogue
import smarthome.raspberry.arduinodevices.domain.controllers.ArduinoDigital
import smarthome.raspberry.arduinodevices.domain.controllers.ArduinoHumidityTemperature
import smarthome.raspberry.arduinodevices.domain.controllers.ArduinoOnOff
import smarthome.raspberry.arduinodevices.domain.state.*
import smarthome.raspberry.channel.api.domain.arduinoChannel
import smarthome.raspberry.devices.api.domain.devicesGson


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
    single(named(devicesGson)) {
        GsonBuilder()
            .withRuntimeFactory(BaseController::class.java,
                mapOf(
                    ArduinoAnalogue::class.java to "ArduinoAnalogue",
                    ArduinoDigital::class.java to "ArduinoDigital",
                    ArduinoHumidityTemperature::class.java to "ArduinoHumidityTemperature",
                    ArduinoOnOff::class.java to "ArduinoOnOff"
                )
            )
            .withRuntimeFactory(IotDevice::class.java, mapOf<Class<out IotDevice>, String>(
                ArduinoDevice::class.java to "ArduinoDevice"
            ))
            .withRuntimeFactory(ControllerState::class.java, mapOf(
                BooleanValueState::class.java to "boolean",
                DoubleValueState::class.java to "double",
                IntValueState::class.java to "int",
                HumidityTemperatureState::class.java to "dht",
                StringValueState::class.java to "string"
            ))
    }
}

fun <T> GsonBuilder.withRuntimeFactory(base: Class<T>,
                                       subtypes: Map<Class<out T>, String>): GsonBuilder {
    return registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory.of(base, typeField).withSubtypes(subtypes)
    )
}

fun <T> RuntimeTypeAdapterFactory<T>.withSubtypes(
    subtypes: Map<Class<out T>, String>): RuntimeTypeAdapterFactory<T> {
    return this.apply { subtypes.forEach { registerSubtype(it.key, it.value) } }
}

val arduinoModule = listOf(
    domain,
    data,
    serverModule
)

private const val deviceGson = "deviceGson"
private const val arduinoApiGson = "arduinoApiGson"
const val arduinoDevices = "arduinoDevicesMap"
const val arduinoControllers = "arduinoControllers"
const val arduinoStates = "arduinoStates"