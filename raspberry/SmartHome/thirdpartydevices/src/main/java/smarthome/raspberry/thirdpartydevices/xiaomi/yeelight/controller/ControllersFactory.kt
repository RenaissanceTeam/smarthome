package smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller

import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.constants.*

object ControllersFactory {

    fun createController(device: YeelightDevice, type: String): Controller {
        return when (type) {
            ADJUST_CONTROLLER_TYPE -> AdjustController(device)
            BRIGHTNESS_CONTROLLER_TYPE -> BrightnessController(device)
            COLOR_TEMPERATURE_CONTROLLER_TYPE -> ColorTemperatureController(device)
            CRON_ADD_CONTROLLER_TYPE -> CronController(device)
            DELETE_CRON_CONTROLLER_TYPE -> DeleteCronController(device)
            FLOW_CONTROLLER_TYPE -> FlowController(device)
            HSV_CONTROLLER_TYPE -> HSVController(device)
            NAME_CONTROLLER_TYPE -> NameController(device)
            POWER_CONTROLLER_TYPE -> PowerController(device)
            RGB_CONTROLLER_TYPE -> RGBController(device)
            TOGGLE_CONTROLLER_TYPE -> ToggleController(device)
            DEFAULT_CONTROLLER_TYPE -> DefaultController(device)
            else -> throw IllegalArgumentException("No such device type $type")
        }
    }

}