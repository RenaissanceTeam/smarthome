package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller

import smarthome.library.common.constants.GATEWAY_ILLUMINATION_CONTROLLER
import smarthome.raspberry.thirdpartydevices.utils.Utils.Companion.adjust
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.command.GatewayLightCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.Gateway
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.net.UdpTransport

class IlluminationController (device: Device, transport: UdpTransport)
    : Controller(device, GATEWAY_ILLUMINATION_CONTROLLER, transport), Writable, Readable {

    /**
     * illumination (int from 0 to 1300)
     */
    override fun write(vararg params: Any) {
        val illumination = adjust(params[0] as Int, 0, 1300)
        super.controllerWrite(GatewayLightCmd((device as Gateway).rgb, illumination))
    }

    override fun read(): String {
        return controllerRead()
    }
}