package smarthome.raspberry.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.library.common.DeviceChannelOutput
import smarthome.library.common.IotDevice

interface HomeRepository {
    suspend fun setupUserInteraction()
    suspend fun setupDevicesInteraction()
    suspend fun setControllerState(controller: BaseController, state: ControllerState)
    suspend fun fetchControllerState(controller: BaseController): ControllerState
    suspend fun onControllerChanged(controller: BaseController)
    suspend fun isHomeIdUnique(homeId: String): Boolean
    fun getCurrentDevices(): MutableList<IotDevice>
    suspend fun proceedReadController(it: BaseController): BaseController
    suspend fun proceedWriteController(it: BaseController): BaseController
    suspend fun applyDeviceChanges(changedDevice: IotDevice)
}
