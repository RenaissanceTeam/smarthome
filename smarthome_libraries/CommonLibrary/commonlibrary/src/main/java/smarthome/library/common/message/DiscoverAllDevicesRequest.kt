package smarthome.library.common.message

import smarthome.library.common.constants.DISCOVER_ALL

class DiscoverAllDevicesRequest(clientId: String = "")
    : Message(clientId = clientId, messageType = DISCOVER_ALL)