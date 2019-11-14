package smarthome.raspberry.devices.api.domain

import smarthome.library.common.IotDevice

/**
 * Triggered when new device should be added to the smarthome.
 *
 * New device is considered to be pending until user explicitly accepts it.
 */
interface AddDeviceUseCase {
    suspend fun execute(device: IotDevice)
}