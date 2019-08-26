package smarthome.library.datalibrary.constants

class WrongMessageType(messageType: String) : RuntimeException("Message type: $messageType is not supported")