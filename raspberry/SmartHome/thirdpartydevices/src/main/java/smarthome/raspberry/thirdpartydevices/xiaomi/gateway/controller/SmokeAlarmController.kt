package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_SMOKE_ALARM_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice

class SmokeAlarmController (device: GatewayDevice) : Controller(device, GATEWAY_SMOKE_ALARM_CONTROLLER), GatewayReadable {

    override fun read(): String {
        return controllerRead()
    }
}