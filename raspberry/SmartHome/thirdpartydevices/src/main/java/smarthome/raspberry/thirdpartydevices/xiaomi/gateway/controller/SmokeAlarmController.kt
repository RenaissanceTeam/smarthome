package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_SMOKE_ALARM_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Device

class SmokeAlarmController (device: Device) : Controller(device, GATEWAY_SMOKE_ALARM_CONTROLLER), Readable {

    override fun read(): String {
        return controllerRead()
    }
}