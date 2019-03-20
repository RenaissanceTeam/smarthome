package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_HUMIDITY_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Device

class HumidityController(device: Device) : Controller(device, GATEWAY_HUMIDITY_CONTROLLER), Readable {

    override fun read(): String {
        return controllerRead()
    }
}