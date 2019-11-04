package smarthome.raspberry.devices.data.storage

import smarthome.library.common.IotDevice

interface LocalDevicesStorage {
    fun saveDevices(devices: MutableList<IotDevice>, group: IotDeviceGroup)
    fun getSavedDevices(group: IotDeviceGroup): List<IotDevice>
    fun updateDevice(device: IotDevice, group: IotDeviceGroup)
    fun add(device: IotDevice, group: IotDeviceGroup)
    fun remove(device: IotDevice, group: IotDeviceGroup)
}

enum class IotDeviceGroup {
    ACTIVE, PENDING
}