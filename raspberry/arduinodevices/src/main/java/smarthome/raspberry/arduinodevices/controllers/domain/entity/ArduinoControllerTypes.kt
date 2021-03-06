package smarthome.raspberry.arduinodevices.controllers.domain.entity

enum class ArduinoControllerTypes(val id: Int, val string: String) {
    analog(1000, "analog"),
    onoff(1001, "onoff"),
    temperature11(1002, "temperature11"),
    humidity11(1003, "humidity11"),
    digital(1004, "digital"),
    temperature22(1006, "temperature22"),
    humidity22(1007, "humidity22"),
    pressure(1008, "pressure")
}