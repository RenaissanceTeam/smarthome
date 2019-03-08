package smarthome.library.datalibrary.store.listeners

import smarthome.library.common.IotDevice

interface DeviceListener {
    fun onDeviceReceived(device: IotDevice)
}