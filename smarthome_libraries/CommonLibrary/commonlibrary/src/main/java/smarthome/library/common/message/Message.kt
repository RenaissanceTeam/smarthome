package smarthome.library.common.message

import smarthome.library.common.GUID

open class Message(val id: String = GUID.guidForMessage,
                   val clientId: String = "",
                   val messageType: String = "")