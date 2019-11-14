package smarthome.raspberry.devices.api.domain

import smarthome.library.common.IotDevice

/**
 * Accepting device means making it a part of the smarthome, after which it should
 * work in it's normal state: user should be able to make read/write requests, create
 * scripts with the device, etc..
 */
interface AcceptPendingDeviceUseCase {
    suspend fun execute(device: IotDevice)
}