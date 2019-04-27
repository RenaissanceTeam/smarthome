package smarthome.raspberry.model

import android.util.Log
import smarthome.library.common.constants.DeviceTypes.GATEWAY_TYPE
import smarthome.library.common.message.*
import smarthome.library.datalibrary.store.listeners.MessageListener
import smarthome.raspberry.App
import smarthome.raspberry.BuildConfig.DEBUG
import smarthome.raspberry.service.DeviceObserver
import smarthome.raspberry.utils.SharedPreferencesHelper

class MessageHandler : MessageListener {


    override fun onMessagesReceived(messages: MutableList<Any>, isInner: Boolean) {
        if (isInner)
            return

        if (messages.size == 0)
            return

        for (message in messages) {
            when (message) {
                is ChangeDeviceStatusRequest -> processChangeDeviceStatusMessage(message)
                is DiscoverAllDevicesRequest -> processDiscoverAllDevicesMessage()
                is DiscoverDeviceRequest -> processDiscoverDeviceMessage(message)
                is ChangeDoNotDisturbOption -> processDoNotDisturbChangeRequest(message)
                else -> if (DEBUG) Log.d(TAG, "Message is not supported")
            }
            SmartHomeRepository.deleteMessage(message as Message)
        }
    }

    private fun processChangeDeviceStatusMessage(message: ChangeDeviceStatusRequest) {
        SmartHomeRepository.changeDeviceStatus(message.deviceId, message.status)
    }

    private fun processDiscoverAllDevicesMessage() {
        DeviceObserver.getInstance().start()
    }

    private fun processDiscoverDeviceMessage(message: DiscoverDeviceRequest) {
        if (message.deviceType == GATEWAY_TYPE) {
            DeviceObserver.getInstance().exploreGateway(message.params)
        }
    }

    private fun processDoNotDisturbChangeRequest(message: ChangeDoNotDisturbOption) {
        SharedPreferencesHelper.getInstance(App.instance.applicationContext)
                .setDoNotDisturb(message.mode)
    }

    companion object {
        private val instance: MessageHandler = MessageHandler()
        val TAG = instance::javaClass.name

        fun getInstance(): MessageHandler {
            return instance
        }

    }
}