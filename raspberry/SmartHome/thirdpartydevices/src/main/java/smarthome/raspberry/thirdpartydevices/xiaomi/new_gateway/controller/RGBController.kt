package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller

import smarthome.library.common.constants.GATEWAY_RGB_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.Gateway
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.command.GatewayLightCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.utils.UdpTransport
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.utils.Utils.calculateRGB
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.utils.Utils.Companion.adjust

class RGBController(device: Device, transport: UdpTransport)
    : Controller(device, GATEWAY_RGB_CONTROLLER, transport), Writable, Readable {

    /**
     * r, g, b (int)
     */
    override fun write(vararg params: Any) {
        val r = adjust(params[0] as Int, 0, 255)
        val g = adjust(params[1] as Int, 0, 255)
        val b = adjust(params[2] as Int, 0, 255)
        val rgb = calculateRGB(r.toByte(), g.toByte(), b.toByte())
        super.controllerWrite(GatewayLightCmd(rgb, (device as Gateway).illumination))
    }

    override fun read(): String {
        return controllerRead()
    }
}