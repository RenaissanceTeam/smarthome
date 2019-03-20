package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller

import smarthome.library.common.constants.GATEWAY_LIGHT_ON_OFF_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.command.GatewayLightCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.STATUS_ON
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.net.UdpTransport
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.utils.Utils.calculateRGB

class GatewayLightOnOffController(device: Device, transport: UdpTransport)
    : Controller(device, GATEWAY_LIGHT_ON_OFF_CONTROLLER, transport), Writable, Readable {

    /**
     * "on", "off" (String)
     */
    override fun write(vararg params: Any) {
        val tmblr = (params[0] as String) == STATUS_ON
        if (tmblr)
            super.controllerWrite(GatewayLightCmd(calculateRGB(255.toByte(), 255.toByte(), 255.toByte()), 1300))
        else super.controllerWrite(GatewayLightCmd(0, 0))
    }

    override fun read(): String {
        return controllerRead()
    }
}