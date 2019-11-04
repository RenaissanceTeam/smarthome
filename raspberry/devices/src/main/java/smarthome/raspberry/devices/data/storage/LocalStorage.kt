package smarthome.raspberry.devices.data.storage

import smarthome.library.common.IotDevice

interface LocalStorage {
    fun updateDevice(device: IotDevice)
    fun addPendingDevice(device: IotDevice)
    fun removePendingDevice(device: IotDevice)
    fun addDevice(device: IotDevice)
    fun removeDevice(device: IotDevice)
    fun getDevices(): List<IotDevice>
}

