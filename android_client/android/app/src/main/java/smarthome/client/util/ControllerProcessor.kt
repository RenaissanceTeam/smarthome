package smarthome.client.util

import smarthome.client.util.Utils.calculateRGB
import smarthome.library.common.BaseController
import smarthome.library.common.constants.*

object ControllerProcessor {

    fun write (controller: BaseController?,
               controllerUpdateHandler: (controller: BaseController?) -> Unit) {
        controller ?: return

        val type = controller.type
        if (type == TOGGLE_CONTROLLER_TYPE ||
                type == POWER_CONTROLLER_TYPE ||
                type == DELETE_CRON_CONTROLLER_TYPE ||
                type == DEFAULT_CONTROLLER_TYPE ||
                type == GATEWAY_LEFT_BUTTON_CONTROLLER ||
                type == GATEWAY_RIGHT_BUTTON_CONTROLLER ||
                type == GATEWAY_SINGLE_BUTTON_CONTROLLER ||
                type == GATEWAY_LIGHT_ON_OFF_CONTROLLER ||
                type == GATEWAY_SMART_PLUG_ON_OFF_CONTROLLER) {
            write(controller, "", controllerUpdateHandler)
        }
    }

    fun write(controller: BaseController?,
              args: String,
              controllerUpdateHandler: (controller: BaseController?) -> Unit) {
        if (controller == null)
            return

        when (controller.type) {
            ADJUST_CONTROLLER_TYPE -> writeParams(controller, args, controllerUpdateHandler)
            BRIGHTNESS_CONTROLLER_TYPE -> writeParams(controller, args, controllerUpdateHandler)
            COLOR_TEMPERATURE_CONTROLLER_TYPE -> writeParams(controller, args, controllerUpdateHandler)
            CRON_ADD_CONTROLLER_TYPE -> writeParams(controller, args, controllerUpdateHandler)
            DELETE_CRON_CONTROLLER_TYPE -> setPendingWrite(controller, controllerUpdateHandler)
            HSV_CONTROLLER_TYPE -> writeParams(controller, args, controllerUpdateHandler)
            NAME_CONTROLLER_TYPE -> writeParams(controller, args, controllerUpdateHandler)
            POWER_CONTROLLER_TYPE -> writeParams(controller, getPowerArgs(controller), controllerUpdateHandler)
            RGB_CONTROLLER_TYPE -> writeParams(controller, getRGBArgs(args), controllerUpdateHandler)
            TOGGLE_CONTROLLER_TYPE -> setPendingWrite(controller, controllerUpdateHandler)
            DEFAULT_CONTROLLER_TYPE -> setPendingWrite(controller, controllerUpdateHandler)

            GATEWAY_BUTTON_CLICK_CONTROLLER -> writeParams(controller, args, controllerUpdateHandler)
            GATEWAY_LEFT_BUTTON_CONTROLLER -> writeParams(controller, "${getPowerArgs(controller)} $STATUS_CHANNEL_1", controllerUpdateHandler)
            GATEWAY_RIGHT_BUTTON_CONTROLLER -> writeParams(controller, "${getPowerArgs(controller)} $STATUS_CHANNEL_0", controllerUpdateHandler)
            GATEWAY_SINGLE_BUTTON_CONTROLLER -> writeParams(controller, getPowerArgs(controller), controllerUpdateHandler)
            GATEWAY_LIGHT_ON_OFF_CONTROLLER -> writeParams(controller, getPowerArgs(controller), controllerUpdateHandler)
            GATEWAY_ILLUMINATION_CONTROLLER -> writeParams(controller, args, controllerUpdateHandler)
            GATEWAY_RGB_CONTROLLER -> writeParams(controller, args, controllerUpdateHandler)
            GATEWAY_SMART_PLUG_ON_OFF_CONTROLLER -> writeParams(controller, getPowerArgs(controller), controllerUpdateHandler)
        }
    }

    fun read (controller: BaseController?,
              controllerUpdateHandler: (controller: BaseController?) -> Unit) {
        if (controller == null)
            return

        controller.setPendingRead()
        controllerUpdateHandler(controller)
    }

    private fun writeParams(controller: BaseController,
                            args: String,
                            controllerUpdateHandler: (controller: BaseController?) -> Unit) {
        controller.setNewState(args)
        setPendingWrite(controller, controllerUpdateHandler)
    }

    private fun setPendingWrite(controller: BaseController,
                                controllerUpdateHandler: (controller: BaseController?) -> Unit) {
        controller.setPendingWrite()
        controllerUpdateHandler(controller)
    }

    private fun getPowerArgs(controller: BaseController): String {
        var params: String = STATUS_OFF
        if (controller.state.isNullOrEmpty() || controller.state.contains(STATUS_OFF))
            params = STATUS_ON

        return params
    }

    private fun getRGBArgs(args: String): String {
        val params = args.split(" ")
        return calculateRGB(params[0], params[1], params[2]).toString()
    }
}