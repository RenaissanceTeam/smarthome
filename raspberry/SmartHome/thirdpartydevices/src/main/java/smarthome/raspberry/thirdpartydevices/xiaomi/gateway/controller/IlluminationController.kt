package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_ILLUMINATION_CONTROLLER
import smarthome.raspberry.thirdpartydevices.utils.Utils.adjust
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces.TransportSettable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.GatewayLightCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Gateway
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport

class IlluminationController (device: GatewayDevice, transport: UdpTransport)
    : Controller(device, GATEWAY_ILLUMINATION_CONTROLLER, transport), GatewayWritable, GatewayReadable, TransportSettable {

    /**
     * illumination (int from 0 to 1300)
     */
    override fun write(params: String) {
        val illumination = adjust(params.toInt(), 0, 1300)
        super.controllerWrite(GatewayLightCmd((device as Gateway).rgb, illumination))
    }

    override fun read(): String {
        return controllerRead()
    }

    override fun setUpTransport(transport: UdpTransport) {
        this.transport = transport
    }
}