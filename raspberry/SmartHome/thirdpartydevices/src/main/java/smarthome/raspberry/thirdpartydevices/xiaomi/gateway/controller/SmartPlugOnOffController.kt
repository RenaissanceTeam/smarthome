package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_SMART_PLUG_ON_OFF_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.SmartPlugCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport

class SmartPlugOnOffController(device: GatewayDevice, transport: UdpTransport)
    : Controller(device, GATEWAY_SMART_PLUG_ON_OFF_CONTROLLER, transport), Readable, Writable {
    /**
     * "on" or "off" (String)
     */
    override fun write(vararg params: Any) {
        super.controllerWrite(SmartPlugCmd(params[0] as String))
    }

    override fun read(): String {
        return controllerRead()
    }
}