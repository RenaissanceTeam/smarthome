package smarthome.raspberry.arduinodevices.data.server.entity

class InvalidDeviceException(json: String, cause: Throwable) : Throwable("Error parsing device=$json", cause)