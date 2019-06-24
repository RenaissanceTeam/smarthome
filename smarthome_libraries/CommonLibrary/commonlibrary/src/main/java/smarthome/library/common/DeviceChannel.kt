package smarthome.library.common

interface DeviceChannel {
    suspend fun read(controller: BaseController): ControllerState
    suspend fun writeState(controller: BaseController, state: ControllerState): ControllerState
}