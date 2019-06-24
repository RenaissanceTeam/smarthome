package smarthome.raspberry.arduinodevices

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.library.common.DeviceChannel
import smarthome.library.common.DeviceChannelOutput

class ArduinoDeviceChannel(private val output: DeviceChannelOutput) : DeviceChannel {

    init {
        // todo start server and push events to output
    }

    override suspend fun read(controller: BaseController): ControllerState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun writeState(controller: BaseController, state: ControllerState): ControllerState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
