package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces

import smarthome.library.common.Device

interface DeviceAddedListener {
    fun onDeviceAdded(device: Device)
}