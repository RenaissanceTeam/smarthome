package smarthome.raspberry.arduinodevices.domain

import android.util.Log
import com.google.gson.Gson
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.library.common.DeviceChannel
import smarthome.library.common.IotDevice
import smarthome.raspberry.arduinodevices.data.ArduinoDeviceApi
import smarthome.raspberry.arduinodevices.data.mapper.ControllerStateToValuePayloadMapper
import smarthome.raspberry.arduinodevices.data.mapper.ValuePayloadToControllerStateMapper
import smarthome.raspberry.arduinodevices.data.server.UdpServer
import smarthome.raspberry.arduinodevices.data.server.api.WebServer
import smarthome.raspberry.arduinodevices.domain.controllers.Writeable
import smarthome.raspberry.home.api.domain.eventbus.ObserveHomeLifecycleUseCase
import smarthome.raspberry.home.api.domain.eventbus.events.Paused
import smarthome.raspberry.home.api.domain.eventbus.events.Resumed

class ArduinoDeviceChannel(
    httpServer: WebServer,
    udpServer: UdpServer,
    observeHomeLifecycleUseCase: ObserveHomeLifecycleUseCase,
    private val arduinoApiGson: Gson,
    private val valueToStateMapper: ValuePayloadToControllerStateMapper,
    private val stateToValueMapper: ControllerStateToValuePayloadMapper
) : DeviceChannel {
    override fun canWorkWith(iotDevice: IotDevice) = iotDevice is ArduinoDevice
    
    init {
        observeHomeLifecycleUseCase.execute().subscribeBy {
    
            when (it) {
                is Paused -> {
                    httpServer.stop()
                    udpServer.stopServer()
                }
                is Resumed -> {
                    httpServer.start()
                    udpServer.startServer()
                }
            }
        }
    }
    
    private fun getArduinoDeviceApi(ip: String): ArduinoDeviceApi {
        return Retrofit.Builder()
            .baseUrl("http://$ip:8080/")
            .addConverterFactory(GsonConverterFactory.create(arduinoApiGson))
            .build()
            .create(ArduinoDeviceApi::class.java)
    }

    override suspend fun read(device: IotDevice, controller: BaseController): ControllerState {
        require(device is ArduinoDevice)

        val api = getArduinoDeviceApi(device.ip)
        val response = api.controllerReadRequest(controller.id)
    
        return valueToStateMapper.map(response.response)
    }

    override suspend fun writeState(device: IotDevice, controller: BaseController, state: ControllerState): ControllerState {
        require(device is ArduinoDevice)
        require(controller is Writeable)

        val api = getArduinoDeviceApi(device.ip)
    
        val writeValue = stateToValueMapper.map(state)
        val response = api.controllerWriteRequest(controller.id, writeValue)
    
        return valueToStateMapper.map(response.response)
    }
}
