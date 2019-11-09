package smarthome.raspberry.devices.data.storage

import smarthome.library.common.IotDevice

interface RemoteStorage {
    suspend fun updateDevice(device: IotDevice)
    suspend fun addPendingDevice(device: IotDevice)
    suspend fun removePendingDevice(device: IotDevice)
    suspend fun addDevice(device: IotDevice)
    suspend fun removeDevice(device: IotDevice)

}

