package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller

import smarthome.library.common.constants.GATEWAY_SMART_PLUG_ON_OFF_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.command.SmartPlugCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.net.UdpTransport

class SmartPlugOnOffController(device: Device, transport: UdpTransport)
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