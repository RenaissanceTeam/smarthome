package smarthome.library.common.message

import smarthome.library.common.constants.DISCOVER_DEVICE

class DiscoverDeviceRequest(clientId: String,
                            val deviceType: String,
                            val params: String)
    : Message(clientId =  clientId, messageType = DISCOVER_DEVICE)