package smarthome.raspberry.devices_api.data

import smarthome.library.common.IotDevice

interface DevicesRepository {
    suspend fun saveDevice(device: IotDevice)
    suspend fun addPendingDevice(device: IotDevice)
    suspend fun removePendingDevice(device: IotDevice)
    suspend fun addDevice(device: IotDevice)
    suspend fun removeDevice(device: IotDevice)
    suspend fun getCurrentDevices(): List<IotDevice>
}