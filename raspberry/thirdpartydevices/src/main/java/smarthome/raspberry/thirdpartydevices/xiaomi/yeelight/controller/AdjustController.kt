package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.library.common.constants.ADJUST_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.READ_NOT_SUPPORTED_MESSAGE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightReadable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class AdjustController(device: YeelightDevice) : Controller(device, ADJUST_CONTROLLER_TYPE), YeelightWritable, YeelightReadable {

    /**
     * modifying device state without knowing the current value
     * @param params {AdjustAction}, {Adjustment}
     */
    override fun write(params: String): Result {
        val options = params.split( " ")
        val action: String = options[0]
        val property: String = options[1]
        return super.controllerWrite(type, action, property)
    }

    override fun read(): String {
        return READ_NOT_SUPPORTED_MESSAGE
    }
}
