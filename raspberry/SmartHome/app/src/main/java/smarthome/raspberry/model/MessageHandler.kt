package smarthome.raspberry.model

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.library.common.constants.DeviceTypes.GATEWAY_TYPE
import smarthome.library.common.message.ChangeDeviceStatusRequest
import smarthome.library.common.message.DiscoverAllDevicesRequest
import smarthome.library.common.message.DiscoverDeviceRequest
import smarthome.library.common.message.Message
import smarthome.library.datalibrary.store.listeners.MessageListener
import smarthome.raspberry.BuildConfig.DEBUG
import smarthome.raspberry.service.DeviceObserver

class MessageHandler : MessageListener {
    override fun invoke(messages: List<Any>, isInnerCall: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isInnerCall || messages.isEmpty())
                return@launch

            for (message in messages) {
                when (message) {
                    is ChangeDeviceStatusRequest -> processChangeDeviceStatusMessage(message)
                    is DiscoverAllDevicesRequest -> processDiscoverAllDevicesMessage()
                    is DiscoverDeviceRequest -> processDiscoverDeviceMessage(message)
                    else -> if (DEBUG) Log.d(TAG, "Message is not supported")
                }
                SmartHomeRepository.deleteMessage(message as Message)
            }
        }
    }

    private suspend fun processChangeDeviceStatusMessage(message: ChangeDeviceStatusRequest) {
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

    companion object {
        private val instance: MessageHandler = MessageHandler()
        val TAG = instance::javaClass.name

        fun getInstance(): MessageHandler {
            return instance
        }

    }
}