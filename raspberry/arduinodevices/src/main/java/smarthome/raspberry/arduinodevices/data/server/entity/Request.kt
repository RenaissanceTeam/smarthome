package smarthome.raspberry.arduinodevices.data.server.entity

data class Request(
    val requestIdentifier: RequestIdentifier,
    val params: Map<String, String> = emptyMap(),
    val body: String = ""
)