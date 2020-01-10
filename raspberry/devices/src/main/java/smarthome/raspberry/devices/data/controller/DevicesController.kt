package smarthome.raspberry.devices.data.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.devices.data.dto.DeviceDetails
import smarthome.raspberry.devices.data.dto.GeneralDeviceInfo
import smarthome.raspberry.devices.data.mapper.DeviceToDeviceDetailsMapper
import smarthome.raspberry.devices.data.mapper.DeviceToGeneralDeviceInfoMapper
import smarthome.raspberry.util.exceptions.notFound

@RestController
@RequestMapping("api/")
class DevicesController(
        private val devicesRepository: DevicesRepository,
        private val deviceGeneralInfoMapper: DeviceToGeneralDeviceInfoMapper,
        private val deviceDetailsMapper: DeviceToDeviceDetailsMapper

) {

    @GetMapping("devices")
    fun getAll(): List<GeneralDeviceInfo> {
        return devicesRepository.findAll().map(deviceGeneralInfoMapper::map)
    }

    @GetMapping("/devices/{id}")
    fun getDetails(@PathVariable id: Long): DeviceDetails {
        val device = devicesRepository.findById(id).runCatching { get() }.getOrElse { throw notFound }
        return deviceDetailsMapper.map(device)
    }
}