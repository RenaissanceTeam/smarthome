package smarthome.raspberry.data

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

interface LocalStorage {
    suspend fun getHomeId(): String
    fun getDevices(): MutableList<IotDevice>
    fun findDevice(controller: BaseController): IotDevice
    suspend fun addDevice(device: IotDevice)
    fun updateDevice(device: IotDevice)
    suspend fun addPendingDevice(device: IotDevice)
    suspend fun removePendingDevice(device: IotDevice)
    suspend fun removeDevice(device: IotDevice)
}