package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller

import smarthome.library.common.constants.GATEWAY_TEMPERATURE_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.Device

class TemperatureController (device: Device) : Controller(device, GATEWAY_TEMPERATURE_CONTROLLER), Readable {

    override fun read(): String {
        return controllerRead()
    }
}