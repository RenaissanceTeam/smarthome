package smarthome.library.common

/**
 * Describes communication from home server to device group. These may be some user requests
 * of scheduled events.
 */
interface DeviceChannel {
    suspend fun read(controller: BaseController): ControllerState
    suspend fun writeState(controller: BaseController, state: ControllerState): ControllerState
}

/**
 * Describes communication from device group to home server.
 *
 * Home should be notified of changes in device group:
 *  new state of controller
 *  add/delete device
 */
interface DeviceChannelOutput {
    suspend fun onNewDevice(device: IotDevice)
    suspend fun onNewState(controller: BaseController)
}