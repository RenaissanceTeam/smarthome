package smarthome.raspberry.data.remote.fcm

import android.content.Context
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.R

object MessageComposer {
    fun createMessageBody(context: Context, controller: BaseController, device: IotDevice,
                          type: MessageType, priority: Priority, tokens: Array<String>) =

            MessageBody(composeAlertTitle(context, controller),
                    composeAlertBody(context, controller, device),
                    tokens, type.toString(), priority.toString())



    private fun composeAlertTitle(context: Context, controller: BaseController): String {
        val base = context.getString(R.string.alert_trigger)
        if (controller.name.isNullOrEmpty()) return base
        return "$base: ${controller.name}"
    }

    private fun composeAlertBody(context: Context, controller: BaseController, device: IotDevice): String {
        return context.getString(R.string.alert_body).format(controller.state, device.name)
    }
}