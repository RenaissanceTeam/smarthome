package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import com.google.firebase.firestore.Exclude
import smarthome.library.common.BaseController
import smarthome.library.common.GUID
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.command.Command
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.defWriteCommandListener
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.WriteCommandListener
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

open class Controller(device: YeelightDevice,
                      type: String,
                      writeCommandListener: WriteCommandListener = defWriteCommandListener)
    : BaseController() {

    @Exclude
    var device: YeelightDevice
        @Exclude get
    private val writeCommandListener: WriteCommandListener

    init {
        this.device = device
        this.writeCommandListener = writeCommandListener
        this.type = type
        this.guid = GUID.getInstance().generateGuidForController(device, this)
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