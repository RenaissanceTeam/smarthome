package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.GATEWAY_SMART_PLUG_ON_OFF_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces.TransportSettable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.SmartPlugCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport

class SmartPlugOnOffController(device: GatewayDevice, transport: UdpTransport)
    : Controller(device, GATEWAY_SMART_PLUG_ON_OFF_CONTROLLER, transport), GatewayReadable, GatewayWritable, TransportSettable {
    /**
     * "on" or "off" (String)
     */
    override fun write(params: String) {
        super.updateState(params)
        super.controllerWrite(SmartPlugCmd(params))
    }

    override fun read(): String {
        return controllerRead()
    }

    override fun setUpTransport(transport: UdpTransport) {
        this.transport = transport
    }
}