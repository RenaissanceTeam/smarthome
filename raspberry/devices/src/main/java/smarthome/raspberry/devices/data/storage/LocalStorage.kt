package smarthome.raspberry.devices.data.storage

import smarthome.library.common.IotDevice

interface LocalStorage {
    fun updateDevice(device: IotDevice, group: IotDeviceGroup)
    fun addDevice(device: IotDevice, group: IotDeviceGroup)
    fun removeDevice(device: IotDevice, group: IotDeviceGroup)
    fun getDevices(group: IotDeviceGroup): List<IotDevice>
    fun getDevices(): List<IotDevice>
    fun getDeviceGroup(device: IotDevice): IotDeviceGroup
}

