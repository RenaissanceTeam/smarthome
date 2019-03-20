package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_DOOR_WINDOW_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Device

class DoorWindowController(device: Device) : Controller(device, GATEWAY_DOOR_WINDOW_CONTROLLER), Readable {

    override fun read(): String {
        return super.controllerRead()
    }
}