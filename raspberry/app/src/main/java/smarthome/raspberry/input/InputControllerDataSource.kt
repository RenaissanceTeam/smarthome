package smarthome.raspberry.input

import smarthome.library.common.DeviceUpdate

interface InputControllerDataSource {
    fun setActionForNewDeviceUpdate(action: (DeviceUpdate) -> Unit)
}