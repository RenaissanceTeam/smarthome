package smarthome.raspberry.data

import io.reactivex.Observable
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

interface LocalStorage {
    fun getHomeId(): Observable<String>
    fun getDevices(): MutableList<IotDevice>
    fun findDevice(controller: BaseController): IotDevice
    suspend fun addDevice(device: IotDevice)
    fun updateDevice(device: IotDevice)
    suspend fun addPendingDevice(device: IotDevice)
    suspend fun removePendingDevice(device: IotDevice)
    suspend fun removeDevice(device: IotDevice)
    fun getPendingDevices(): MutableList<IotDevice>
}