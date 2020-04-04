package smarthome.raspberry.arduinodevices.data.api

interface ArduinoDeviceApi {
    fun readController(controllerIndex: Int): String

    fun writeStateToController(controllerIndex: Int, value: String): String
}


