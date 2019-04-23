package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

interface AlarmHandler {
    fun onAlarm(device: IotDevice, controller: BaseController)
}