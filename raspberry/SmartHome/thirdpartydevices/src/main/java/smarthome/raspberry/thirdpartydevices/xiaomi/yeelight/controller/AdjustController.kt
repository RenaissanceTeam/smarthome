package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.ADJUST_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.AdjustAction
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Adjustment
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class AdjustController(device: Device) : Controller(device, ADJUST_CONTROLLER_TYPE), Writable {

    /**
     * modifying device state without knowing the current value
     * @param params {AdjustAction}, {Adjustment}
     */
    override fun write(vararg params: Any): Result {
        val action: String = (params[0] as Adjustment).action
        val property: String = (params[0] as AdjustAction).action
        return super.controllerWrite(ADJUST_CONTROLLER_TYPE, action, property)
    }
}