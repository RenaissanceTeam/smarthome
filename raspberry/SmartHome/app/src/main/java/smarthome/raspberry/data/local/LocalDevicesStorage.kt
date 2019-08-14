package smarthome.raspberry.data.local

import smarthome.library.common.IotDevice

interface LocalDevicesStorage {
    fun saveDevices(devices: List<IotDevice>)
    fun getSavedDevices(): List<IotDevice>
}