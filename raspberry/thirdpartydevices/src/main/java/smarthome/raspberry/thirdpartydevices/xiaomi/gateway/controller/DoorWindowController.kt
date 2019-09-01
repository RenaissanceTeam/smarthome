package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_DOOR_WINDOW_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice

class DoorWindowController(device: GatewayDevice) : Controller(device, GATEWAY_DOOR_WINDOW_CONTROLLER), GatewayReadable {

    override fun read(): String {
        return super.controllerRead()
    }
}