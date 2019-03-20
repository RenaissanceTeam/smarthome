package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_RGB_CONTROLLER
import smarthome.raspberry.thirdpartydevices.utils.Utils.Companion.adjust
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.GatewayLightCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Gateway
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.utils.Utils.calculateRGB

class RGBController(device: Device, transport: UdpTransport)
    : Controller(device, GATEWAY_RGB_CONTROLLER, transport), Writable, Readable {

    /**
     * r, g, b (int)
     */
    override fun write(vararg params: Any) {
        if(params.size > 1) {
            val r = adjust(params[0] as Int, 0, 255)
            val g = adjust(params[1] as Int, 0, 255)
            val b = adjust(params[2] as Int, 0, 255)
            val rgb = calculateRGB(r.toByte(), g.toByte(), b.toByte())
            super.controllerWrite(GatewayLightCmd(rgb, (device as Gateway).illumination))
        } else {
            super.controllerWrite(GatewayLightCmd(params[0] as Long, (device as Gateway).illumination))
        }
    }

    override fun read(): String {
        return controllerRead()
    }
}