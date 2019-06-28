package smarthome.raspberry.arduinodevices

import smarthome.library.common.BaseController
import smarthome.library.common.DeviceServeState
import smarthome.library.common.IotDevice

class ArduinoDevice(name: String,
                    description: String?,
                    serveState: DeviceServeState,
                    guid: Long,
                    controllers: List<BaseController>,
                    val ip: String)
    : IotDevice(name, description, serveState, guid, controllers)