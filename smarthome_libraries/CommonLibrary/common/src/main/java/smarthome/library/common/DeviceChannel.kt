package smarthome.library.common

/**
 * Describes communication from home server to device group. These may be some user requests
 * or scheduled events.
 */
interface DeviceChannel {
    suspend fun read(device: IotDevice, controller: BaseController): ControllerState
    suspend fun writeState(device: IotDevice, controller: BaseController, state: ControllerState): ControllerState
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
    suspend fun findController(guid: Long): BaseController
    suspend fun findDevice(controller: BaseController): IotDevice
}