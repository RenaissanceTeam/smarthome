package smarthome.raspberry.data

import io.reactivex.Observable
import smarthome.library.common.DeviceUpdate
import smarthome.library.common.IotDevice

interface RemoteStorage {
    suspend fun init()
    suspend fun updateDevice(device: IotDevice)
    suspend fun createHome(homeId: String)
    suspend fun isHomeIdUnique(homeId: String): Boolean
    suspend fun getDevices(): Observable<DeviceUpdate>
    suspend fun addPendingDevice(device: IotDevice)
    suspend fun removePendingDevice(device: IotDevice)
    suspend fun addDevice(device: IotDevice)
    suspend fun removeDevice(device: IotDevice)
}