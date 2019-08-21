package smarthome.raspberry.data

import smarthome.library.common.IotDevice

interface RemoteStorage {
    suspend fun updateDevice(device: IotDevice)
    suspend fun createHome(homeId: String)
    suspend fun isHomeIdUnique(homeId: String): Boolean
    suspend fun addPendingDevice(device: IotDevice)
    suspend fun removePendingDevice(device: IotDevice)
    suspend fun addDevice(device: IotDevice)
    suspend fun removeDevice(device: IotDevice)
}

