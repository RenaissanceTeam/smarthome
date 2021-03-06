package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_TEMPERATURE_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice

class TemperatureController (device: GatewayDevice) : Controller(device, GATEWAY_TEMPERATURE_CONTROLLER), GatewayReadable {

    override fun read(): String {
        return controllerRead()
    }
}