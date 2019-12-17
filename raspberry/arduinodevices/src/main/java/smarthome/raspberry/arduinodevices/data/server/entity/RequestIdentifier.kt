package smarthome.raspberry.arduinodevices.data.server.entity


data class RequestIdentifier(
    val method: Method,
    val uri: String
)