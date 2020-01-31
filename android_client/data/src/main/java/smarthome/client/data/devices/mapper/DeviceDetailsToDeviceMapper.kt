package smarthome.client.data.devices.mapper

import smarthome.client.data.devices.dto.DeviceDetails
import smarthome.client.entity.Device

class DeviceDetailsToDeviceMapper {
    fun map(details: DeviceDetails) = Device(
        id = details.id,
        name = details.name,
        description = details.description,
        type = details.type,
        controllers = details.controllers.map { it.id }
    )
}