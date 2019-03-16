package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerType
import smarthome.library.common.GUID
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.command.Command
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

open class Controller(val device: Device, type: String) : BaseController() {

    init {
        this.guid = GUID.getInstance().issueNewControllerGuid(this)
        this.deviceType = type
    }

    fun controllerWrite(method: String, vararg params: Any): Result {
        return device.sendCommand(Command(method, params))
    }

    fun controllerRead(property: Property): String {
        return device.getProperty(property)
    }

}