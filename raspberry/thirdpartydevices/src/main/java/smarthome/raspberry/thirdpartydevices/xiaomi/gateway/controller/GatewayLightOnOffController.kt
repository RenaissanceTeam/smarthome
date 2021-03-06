package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_LIGHT_ON_OFF_CONTROLLER
import smarthome.library.common.constants.STATUS_ON
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces.TransportSettable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.GatewayLightCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.utils.Utils.calculateRGB

class GatewayLightOnOffController(device: GatewayDevice, transport: UdpTransport)
    : Controller(device, GATEWAY_LIGHT_ON_OFF_CONTROLLER, transport), GatewayWritable, GatewayReadable, TransportSettable {

    /**
     * "on", "off" (String)
     */
    override fun write(params: String) {
        super.updateState(params)
        val tmblr = params == STATUS_ON
        if (tmblr)
            super.controllerWrite(GatewayLightCmd(calculateRGB(255.toByte(), 255.toByte(), 255.toByte()), 1300))
        else super.controllerWrite(GatewayLightCmd(0, 0))
    }

    override fun read(): String {
        return controllerRead()
    }

    override fun setUpTransport(transport: UdpTransport) {
        this.transport = transport
    }
}