package smarthome.raspberry.devices.data.mapper


import org.springframework.stereotype.Component
import smarthome.raspberry.controllers.api.data.mapper.ControllerToGeneralControllerInfoMapper
import smarthome.raspberry.devices.api.data.dto.DeviceDetails
import smarthome.raspberry.entity.device.Device

@Component
open class DeviceToDeviceDetailsMapper(
        private val controllerMapper: ControllerToGeneralControllerInfoMapper
) {
    fun map(device: Device) = DeviceDetails(
            id = device.id,
            serial = device.serial,
            name = device.name,
            description = device.description,
            type = device.type,
            controllers = device.controllers.map(controllerMapper::map)
    )
}
