package smarthome.library.common

import io.reactivex.Observable

interface SmartHomeStorage {
    suspend fun createSmartHome()
    suspend fun postSmartHome(smartHome: SmartHome)
    suspend fun getSmartHome(): SmartHome
    suspend fun addDevice(iotDevice: IotDevice)
    suspend fun updateDevice(device: IotDevice)
    suspend fun getDevice(guid: Long): IotDevice
    suspend fun removeDevice(iotDevice: IotDevice)
    suspend fun observeDevicesUpdates(): Observable<DeviceUpdate>
    suspend fun addPendingDevice(iotDevice: IotDevice)
    suspend fun updatePendingDevice(device: IotDevice)
    suspend fun removePendingDevice(iotDevice: IotDevice)
    suspend fun fetchPendingDevices(): MutableList<IotDevice>
    suspend fun observePendingDevicesUpdates(): Observable<DeviceUpdate>
}