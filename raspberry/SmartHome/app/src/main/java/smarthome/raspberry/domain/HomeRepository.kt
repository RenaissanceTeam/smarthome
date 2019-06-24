package smarthome.raspberry.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.library.common.DeviceChannelOutput

interface HomeRepository {
    suspend fun setupUserInteraction()
    suspend fun setupDevicesInteraction()
    suspend fun setControllerState(controller: BaseController, state: ControllerState)
    suspend fun fetchControllerState(controller: BaseController): ControllerState
    suspend fun onControllerChanged(controller: BaseController)
}
