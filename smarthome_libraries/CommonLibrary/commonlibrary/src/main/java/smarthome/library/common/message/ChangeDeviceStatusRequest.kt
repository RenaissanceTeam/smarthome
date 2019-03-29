package smarthome.library.common.message

import smarthome.library.common.constants.CHANGE_DEVICE_STATUS

class ChangeDeviceStatusRequest(
        val deviceId: Long,
        val status: String,
        clientId: String)
    : Message(clientId = clientId, messageType = CHANGE_DEVICE_STATUS)
