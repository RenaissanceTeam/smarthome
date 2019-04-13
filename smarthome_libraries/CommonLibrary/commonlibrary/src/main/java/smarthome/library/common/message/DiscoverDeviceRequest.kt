package smarthome.library.common.message

import smarthome.library.common.constants.DISCOVER_DEVICE
import smarthome.library.common.constants.DeviceTypes.DEFAULT_TYPE

class DiscoverDeviceRequest(clientId: String = "",
                            val deviceType: String = DEFAULT_TYPE,
                            val params: String = "")
    : Message(clientId =  clientId, messageType = DISCOVER_DEVICE)