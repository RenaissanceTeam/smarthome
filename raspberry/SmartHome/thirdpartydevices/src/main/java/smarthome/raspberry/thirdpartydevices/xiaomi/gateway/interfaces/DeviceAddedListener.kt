package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces

import smarthome.library.common.IotDevice

interface DeviceAddedListener {
    fun onDeviceAdded(device: IotDevice)
}