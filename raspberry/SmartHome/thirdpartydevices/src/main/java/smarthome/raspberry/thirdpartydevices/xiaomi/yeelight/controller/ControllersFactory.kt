package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.Device
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.enums.Property

object ControllersFactory {

    fun createController(device: Device, type: Property): Controller {
        if (type == Property.COLOR_TEMPERATURE)
            return ColorTemperatureController(device)
        else
            throw IllegalArgumentException("No such device type")
    }

}