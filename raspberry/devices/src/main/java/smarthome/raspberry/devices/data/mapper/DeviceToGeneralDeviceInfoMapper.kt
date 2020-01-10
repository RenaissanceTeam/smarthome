package smarthome.raspberry.devices.data.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.data.dto.GeneralDeviceInfo
import smarthome.raspberry.entity.Device

@Component
open class DeviceToGeneralDeviceInfoMapper(
        private val controllerMapper: ControllerToGeneralControllerInfoMapper
) {
    fun map(device: Device) = GeneralDeviceInfo(
            device.id,
            device.name,
            device.type,
            device.controllers.map(controllerMapper::map)
    )
}
