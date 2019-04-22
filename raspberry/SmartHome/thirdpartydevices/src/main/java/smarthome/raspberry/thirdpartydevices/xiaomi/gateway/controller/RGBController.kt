package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_RGB_CONTROLLER
import smarthome.raspberry.thirdpartydevices.utils.Utils.adjust
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces.TransportSettable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.GatewayLightCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Gateway
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.utils.Utils.calculateRGB

class RGBController(device: GatewayDevice, transport: UdpTransport)
    : Controller(device, GATEWAY_RGB_CONTROLLER, transport), GatewayWritable, GatewayReadable, TransportSettable {

    /**
     * r, g, b (int) or calculated on client rgb (long)
     */
    override fun write(params: String) {
        super.updateState(params)
        val args = params.split(" ")
        if(args.size > 1) {
            val r = adjust(args[0].toInt(), 0, 255)
            val g = adjust(args[1].toInt(), 0, 255)
            val b = adjust(args[2].toInt(), 0, 255)
            val rgb = calculateRGB(r.toByte(), g.toByte(), b.toByte())
            super.controllerWrite(GatewayLightCmd(rgb, (device as Gateway).illumination))
        } else {
            super.controllerWrite(GatewayLightCmd(params.toLong(), (device as Gateway).illumination))
        }
    }

    override fun read(): String {
        return controllerRead()
    }

    override fun setUpTransport(transport: UdpTransport) {
        this.transport = transport
    }
}