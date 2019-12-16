package smarthome.raspberry.arduinodevices.domain

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class ArduinoDevice(
    name: String,
    description: String?,
    controllers: List<BaseController>,
    val ip: String
) : IotDevice(name, description, controllers = controllers)