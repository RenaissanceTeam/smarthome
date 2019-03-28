package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_BUTTON_CLICK_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.WirelessSwitchCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport

class ButtonClickController(device: GatewayDevice, transport: UdpTransport)
    : Controller(device, GATEWAY_BUTTON_CLICK_CONTROLLER, transport), GatewayReadable, GatewayWritable {
    /**
     * STATUS_CLICK, STATUS_DOUBLE_CLICK, STATUS_LONG_PRESS
     */
    override fun write(params: String) {
        super.controllerWrite(WirelessSwitchCmd(params))
    }

    override fun read(): String {
        return controllerRead()
    }
}