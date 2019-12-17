package smarthome.raspberry.arduinodevices.domain

import com.google.gson.GsonBuilder
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smarthome.library.common.*
import smarthome.raspberry.arduinodevices.data.ArduinoDeviceApi
import smarthome.raspberry.arduinodevices.domain.controllers.ArduinoController
import smarthome.raspberry.arduinodevices.data.server.UdpServer
import smarthome.raspberry.arduinodevices.data.server.api.WebServer
import smarthome.raspberry.home.api.domain.lifecycle.ObserveHomeLifecycleUseCase
import smarthome.raspberry.home.api.domain.lifecycle.Paused
import smarthome.raspberry.home.api.domain.lifecycle.Resumed

class ArduinoDeviceChannel(
    httpServer: WebServer,
    udpServer: UdpServer,
    observeHomeLifecycleUseCase: ObserveHomeLifecycleUseCase
) : DeviceChannel {
    
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
        val gson = GsonBuilder()
                .setLenient()
                .create()

        return Retrofit.Builder()
                .baseUrl("http://$ip:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ArduinoDeviceApi::class.java)
    }


    override suspend fun read(device: IotDevice, controller: BaseController): ControllerState {
        require(device is ArduinoDevice)
        require(controller is ArduinoController)

        val api = getArduinoDeviceApi(device.ip)
        val response = api.controllerReadRequest(controller.indexInArduinoServicesArray)

        return controller.parser.parse(response)
    }

    override suspend fun writeState(device: IotDevice, controller: BaseController, state: ControllerState): ControllerState {
        require(device is ArduinoDevice)
        require(controller is ArduinoController)

        val api = getArduinoDeviceApi(device.ip)

        val writeValue = controller.parser.encode(state)
        val response = api.controllerWriteRequest(controller.indexInArduinoServicesArray, writeValue)

        return controller.parser.parse(response)
    }
}
