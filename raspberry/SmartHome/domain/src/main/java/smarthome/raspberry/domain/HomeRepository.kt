package smarthome.raspberry.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.library.common.IotDevice


interface HomeRepository {
    suspend fun setupUserInteraction()
    suspend fun onControllerChanged(controller: BaseController)
    suspend fun isHomeIdUnique(homeId: String): Boolean
    fun getCurrentDevices(): MutableList<IotDevice>
    suspend fun proceedReadController(controller: BaseController): BaseController
    suspend fun proceedWriteController(controller: BaseController, state: ControllerState): BaseController
    suspend fun saveDevice(device: IotDevice)
    suspend fun addPendingDevice(device: IotDevice)
    suspend fun removePendingDevice(device: IotDevice)
    suspend fun addDevice(device: IotDevice)
    suspend fun removeDevice(device: IotDevice)
}
