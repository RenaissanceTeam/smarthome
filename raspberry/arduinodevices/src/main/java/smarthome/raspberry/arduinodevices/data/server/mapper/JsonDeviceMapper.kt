package smarthome.raspberry.arduinodevices.data.server.mapper

import smarthome.library.common.IotDevice

interface JsonDeviceMapper {
    fun map(device: String): IotDevice
}