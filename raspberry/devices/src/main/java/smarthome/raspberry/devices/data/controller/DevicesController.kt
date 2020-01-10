package smarthome.raspberry.devices.data.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.devices.data.dto.GeneralDeviceInfo
import smarthome.raspberry.devices.data.mapper.DeviceToGeneralDeviceInfoMapper
import smarthome.raspberry.entity.Device

@RestController
@RequestMapping("api/")
class DevicesController(
        private val devicesRepository: DevicesRepository,
        private val deviceMapper: DeviceToGeneralDeviceInfoMapper
) {

    @GetMapping("devices")
    fun getAll(): List<GeneralDeviceInfo> {
        return devicesRepository.findAll().map(deviceMapper::map)
    }
}