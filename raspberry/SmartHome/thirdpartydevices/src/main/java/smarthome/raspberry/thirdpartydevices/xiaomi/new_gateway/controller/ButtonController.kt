package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.command.WiredDualWallSwitchCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.command.WiredSingleWallSwitchCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.constants.STATUS_CHANNEL_0
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.WiredDualWallSwitch
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.WiredSingleWallSwitch

class ButtonController(device: Device, type: String) : Controller(device, type), Readable, Writable {
    /**
     * for WiredDualWallSwitch: "on" or "off" (String), status_channel (String)
     * for WiredSingleWallSwitch: "on" or "off" (String)
     */
    override fun write(vararg params: Any) {
        when (device) {
            is WiredDualWallSwitch -> {
                if(params[1] as String == STATUS_CHANNEL_0)
                    sendCommand(params[0] as String, device.statusRight)
                else sendCommand(device.statusLeft, params[0] as String)
            }
            is WiredSingleWallSwitch -> WiredSingleWallSwitchCmd(params[0] as String)
        }
    }

    override fun read(): String {
        return controllerRead()
    }

    private fun sendCommand(statusLeft: String, statusRight: String) {
        super.controllerWrite(WiredDualWallSwitchCmd(statusLeft, statusRight))
    }
}