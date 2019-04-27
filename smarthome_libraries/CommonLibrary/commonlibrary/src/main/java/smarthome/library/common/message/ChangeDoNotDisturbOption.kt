package smarthome.library.common.message

import smarthome.library.common.constants.DO_NOT_DISTURB_MODE

class ChangeDoNotDisturbOption(clientId : String,
                               val mode: Boolean)
    : Message(clientId = clientId, messageType = DO_NOT_DISTURB_MODE)