package smarthome.raspberry.arduinodevices.data

interface ArduinoDeviceApi {
    fun readController(controllerIndex: Int): String

    fun writeStateToController(
            controllerIndex: Int, value: String): String
}


