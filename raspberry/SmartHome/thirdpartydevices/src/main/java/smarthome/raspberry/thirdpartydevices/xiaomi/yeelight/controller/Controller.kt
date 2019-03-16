package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.library.common.BaseController
import smarthome.library.common.ControllerType
import smarthome.library.common.GUID
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.command.Command
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.defWriteCommandListener
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Readable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.WriteCommandListener
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

open class Controller(val device: Device,
                      type: String,
                      val writeCommandListener: WriteCommandListener = defWriteCommandListener)
    : BaseController() {

    init {
        this.guid = GUID.getInstance().issueNewControllerGuid(this)
        this.deviceType = type
    }

    fun controllerWrite(method: String, vararg params: Any): Result {
        val res: Result = device.sendCommand(Command(method, params))
        writeCommandListener.onWriteCompleted(res)
        return res
    }

    fun controllerRead(property: Property): String {
        val res: String = device.getProperty(property)
        setNewState(res)
        return res
    }

    fun controllerRead(vararg properties: Property): Array<String> {
        return device.getProperties(*properties)
    }
}