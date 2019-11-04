package smarthome.raspberry.devices_api.domain

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerServeState
import smarthome.library.common.ControllerState
import smarthome.library.common.IotDevice
import smarthome.raspberry.devices_api.data.DevicesRepository

interface DevicesService {

    /**
     * Triggered when new device should be added to the smarthome.
     *
     * New device is considered to be pending until user explicitly accepts it.
     */
    suspend fun addNewDevice(device: IotDevice)

    /**
     * Accepting device means making it a part of the smarthome, after which it should
     * work in it's normal state: user should be able to make read/write requests, create
     * scripts with the device, etc..
     */
    suspend fun acceptPendingDevice(device: IotDevice)

    suspend fun removeDevice(device: IotDevice)
    suspend fun saveDevice(device: IotDevice)
}

