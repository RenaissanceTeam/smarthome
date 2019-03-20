package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_TIME_IN_USE_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Device

class TimeInUseController(device: Device) : Controller(device, GATEWAY_TIME_IN_USE_CONTROLLER), Readable {

    override fun read(): String {
        return controllerRead()
    }
}