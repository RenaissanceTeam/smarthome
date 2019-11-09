package smarthome.raspberry.input.data

import smarthome.library.common.DeviceUpdate

interface InputControllerDataSource {
    fun setActionForNewDeviceUpdate(action: (DeviceUpdate) -> Unit)
}