package smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller

import smarthome.library.common.constants.GATEWAY_BUTTON_CLICK_CONTROLLER
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.command.WirelessSwitchCmd
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.new_gateway.device.Device

class ButtonClickController(device: Device) : Controller(device, GATEWAY_BUTTON_CLICK_CONTROLLER), Readable, Writable {
    /**
     * STATUS_CLICK, STATUS_DOUBLE_CLICK, STATUS_LONG_PRESS
     */
    override fun write(vararg params: Any) {
        super.controllerWrite(WirelessSwitchCmd(params[0] as String))
    }

    override fun read(): String {
        return controllerRead()
    }
}