package smarthome.client.presentation.fragments.deviceaddition

import smarthome.library.common.constants.DeviceTypes.GATEWAY_TYPE
import smarthome.library.common.message.DiscoverAllDevicesRequest
import smarthome.library.common.message.DiscoverDeviceRequest
import smarthome.library.common.message.Message

object MessageFactory {

    fun createMessage(discoverMethod: String, args: String?): Message {
        return when (discoverMethod) {
            SEARCH_ALL_METHOD -> DiscoverAllDevicesRequest()
            SEARCH_GATEWAY_METHOD -> DiscoverDeviceRequest(deviceType = GATEWAY_TYPE, params = args!!)
            else -> throw IllegalArgumentException("Invalid discover method")
        }
    }

}