package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.ADJUST_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.YeelightWritable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class AdjustController(device: YeelightDevice) : Controller(device, ADJUST_CONTROLLER_TYPE), YeelightWritable {

    /**
     * modifying device state without knowing the current value
     * @param params {AdjustAction}, {Adjustment}
     */
    override fun write(params: String): Result {
        val options = params.split( " ")
        val action: String = options[0]
        val property: String = options[1]
        return super.controllerWrite(ADJUST_CONTROLLER_TYPE, action, property)
    }
}