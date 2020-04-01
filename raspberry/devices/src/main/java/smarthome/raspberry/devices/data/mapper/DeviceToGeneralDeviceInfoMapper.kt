package smarthome.raspberry.devices.data.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.controllers.api.data.mapper.ControllerToGeneralControllerInfoMapper
import smarthome.raspberry.devices.api.data.dto.GeneralDeviceInfo
import smarthome.raspberry.entity.device.Device

@Component
open class DeviceToGeneralDeviceInfoMapper(
        private val controllerMapper: ControllerToGeneralControllerInfoMapper
) {
    fun map(device: Device) = GeneralDeviceInfo(
            id = device.id,
            name = device.name,
            type = device.type,
            controllers = device.controllers.map(controllerMapper::map)
    )
}
