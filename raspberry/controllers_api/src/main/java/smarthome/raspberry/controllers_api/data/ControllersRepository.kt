package smarthome.raspberry.controllers_api.data

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.library.common.IotDevice

interface ControllersRepository {
    suspend fun onControllerChanged(controller: BaseController)
    suspend fun isHomeIdUnique(homeId: String): Boolean
    fun getCurrentDevices(): MutableList<IotDevice>
    suspend fun proceedReadController(controller: BaseController): BaseController
    suspend fun proceedWriteController(controller: BaseController,
                                       state: ControllerState): BaseController
}