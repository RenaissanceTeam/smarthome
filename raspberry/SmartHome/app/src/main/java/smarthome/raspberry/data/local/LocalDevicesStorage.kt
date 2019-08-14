package smarthome.raspberry.data.local

import smarthome.library.common.IotDevice

interface LocalDevicesStorage {
    fun saveDevices(devices: List<IotDevice>)
    fun getSavedDevices(): List<IotDevice>
    fun updateDevice(device: IotDevice)
    fun add(device: IotDevice)
    fun addPending(device: IotDevice)
    fun removePending(device: IotDevice)
    fun remove(device: IotDevice)
}