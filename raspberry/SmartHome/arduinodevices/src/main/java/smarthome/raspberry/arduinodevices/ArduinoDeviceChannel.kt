package smarthome.raspberry.arduinodevices

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import smarthome.library.common.*
import smarthome.raspberry.arduinodevices.controllers.ArduinoController
import smarthome.raspberry.arduinodevices.server.StoppableServer
import smarthome.raspberry.arduinodevices.server.UdpServer
import smarthome.raspberry.arduinodevices.server.WebServer

class ArduinoDeviceChannel(private val output: DeviceChannelOutput) : DeviceChannel {
    private val communicationServer: WebServer = TODO()
    private val initilizationServer: UdpServer = TODO()

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

    init {
        // todo start server and push events to output
        initilizationServer.startServer()
    }

    private fun setupWebServer() {
        communicationServer


        communicationServer.startServer()

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
