package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.FLOW_CONTROLLER_TYPE
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.interfaces.Writable
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.result.Result

class FlowController(device: Device) : Controller(device, FLOW_CONTROLLER_TYPE), Writable {

    override fun write(vararg params: Any): Result {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}