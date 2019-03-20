package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_BUTTON_CLICK_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.WirelessSwitchCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport

class ButtonClickController(device: Device, transport: UdpTransport)
    : Controller(device, GATEWAY_BUTTON_CLICK_CONTROLLER, transport), Readable, Writable {
    /**
     * STATUS_CLICK, STATUS_DOUBLE_CLICK, STATUS_LONG_PRESS
     */
    override fun write(vararg params: Any) {
        super.controllerWrite(WirelessSwitchCmd(params[0] as String))
    }

    override fun read(): String {
        return controllerRead()
    }
}