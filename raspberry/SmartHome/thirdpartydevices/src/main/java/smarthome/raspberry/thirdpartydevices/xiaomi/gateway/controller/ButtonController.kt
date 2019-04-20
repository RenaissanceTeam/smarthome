package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.constants.STATUS_CHANNEL_0
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.interfaces.TransportSettable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.WiredDualWallSwitchCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.WiredSingleWallSwitchCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.interfaces.GatewayWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.WiredDualWallSwitch
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.WiredSingleWallSwitch
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport

class ButtonController(device: GatewayDevice, type: String, transport: UdpTransport)
    : Controller(device, type, transport), GatewayReadable, GatewayWritable, TransportSettable {
    /**
     * for WiredDualWallSwitch: "on" or "off" (String), status_channel (String)
     * for WiredSingleWallSwitch: "on" or "off" (String)
     */
    override fun write(params: String) {
        when (device) {
            is WiredDualWallSwitch -> {
                val args = params.split(" ")
                if(args[1] == STATUS_CHANNEL_0)
                    sendCommand(args[0], (device as WiredDualWallSwitch).statusRight)
                else sendCommand((device as WiredDualWallSwitch).statusLeft, args[0])
            }
            is WiredSingleWallSwitch -> controllerWrite(WiredSingleWallSwitchCmd(params))
        }
    }

    override fun read(): String {
        return controllerRead()
    }

    private fun sendCommand(statusLeft: String, statusRight: String) {
        super.controllerWrite(WiredDualWallSwitchCmd(statusLeft, statusRight))
    }

    override fun setUpTransport(transport: UdpTransport) {
        this.transport = transport
    }
}