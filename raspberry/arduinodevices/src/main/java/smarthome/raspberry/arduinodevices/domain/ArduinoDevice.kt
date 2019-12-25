package smarthome.raspberry.arduinodevices.domain

import smarthome.library.common.BaseController
import smarthome.library.common.Id
import smarthome.library.common.IotDevice

open class ArduinoDevice(
    id: Id,
    name: String,
    description: String?,
    controllers: List<BaseController>,
    val ip: String
) : IotDevice(name, description, controllers = controllers)