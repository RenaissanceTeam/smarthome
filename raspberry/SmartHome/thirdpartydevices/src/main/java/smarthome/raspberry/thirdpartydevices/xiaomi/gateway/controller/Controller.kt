package smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller

import smarthome.library.common.BaseController
import smarthome.library.common.GUID
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.command.Command
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.defStateChangeListener
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.controller.listeners.StateChangeListener
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.net.UdpTransport

open class Controller(val device: GatewayDevice,
                      type: String,
                      private val transport: UdpTransport? = null,
                      private val stateChangedListener: StateChangeListener = defStateChangeListener)
    : BaseController() {

    init {
        this.type = type
        this.guid = GUID.getInstance().generateGuidForController(device, this)
    }

    fun controllerWrite(command: Command) {
        transport?.sendWriteCommand(device.sid, device.type, command)
    }

    fun controllerRead(): String {
        return state
    }

}