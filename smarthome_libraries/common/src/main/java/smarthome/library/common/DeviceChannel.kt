package smarthome.library.common

/**
 * Describes communication from home server to device group. These may be some user requests
 * or scheduled events.
 */
interface DeviceChannel {
    suspend fun read(device: IotDevice, controller: BaseController): ControllerState
    suspend fun writeState(device: IotDevice, controller: BaseController, state: ControllerState): ControllerState
}