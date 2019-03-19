package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller

import smarthome.library.common.constants.GATEWAY_POWER_CONSUMED_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.Device

class PowerConsumedController (device: Device) : Controller(device, GATEWAY_POWER_CONSUMED_CONTROLLER), Readable {

    override fun read(): String {
        return controllerRead()
    }
}