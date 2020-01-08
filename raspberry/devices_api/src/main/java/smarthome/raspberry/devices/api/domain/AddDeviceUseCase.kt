package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.devices.api.domain.dto.DeviceDTO

/**
 * Triggered when new device should be added to the smarthome.
 *
 * New device is considered to be pending until user explicitly accepts it.
 */
interface AddDeviceUseCase {
    fun execute(device: DeviceDTO)
}