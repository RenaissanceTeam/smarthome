package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces

import smarthome.library.common.Controller
import smarthome.library.common.Device

interface AlarmHandler {
    fun onAlarm(device: Device, controller: Controller)
}