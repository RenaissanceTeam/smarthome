package smarthome.raspberry.devices.data.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.devices.api.data.dto.DeviceDetails
import smarthome.raspberry.devices.api.data.dto.GeneralDeviceInfo
import smarthome.raspberry.devices.api.domain.GetActiveDevicesUseCase
import smarthome.raspberry.devices.api.domain.GetDeviceByIdUseCase
import smarthome.raspberry.devices.api.domain.GetDevicesUseCase
import smarthome.raspberry.devices.api.domain.GetPendingDevicesUseCase
import smarthome.raspberry.devices.data.mapper.DeviceToDeviceDetailsMapper
import smarthome.raspberry.devices.data.mapper.DeviceToGeneralDeviceInfoMapper
import smarthome.raspberry.util.exceptions.notFound

@RestController
@RequestMapping("api/")
class DevicesController(
        private val getDevicesUseCase: GetDevicesUseCase,
        private val getPendingDevicesUseCase: GetPendingDevicesUseCase,
        private val getActiveDevicesUseCase: GetActiveDevicesUseCase,
        private val deviceGeneralInfoMapper: DeviceToGeneralDeviceInfoMapper,
        private val getDeviceByIdUseCase: GetDeviceByIdUseCase,
        private val deviceDetailsMapper: DeviceToDeviceDetailsMapper

) {

    @GetMapping("devices")
    fun getAll(): List<GeneralDeviceInfo> {
        return getDevicesUseCase.execute().map(deviceGeneralInfoMapper::map)
    }

    @GetMapping("devices/pending")
    fun getPending(): List<GeneralDeviceInfo> {
        return getPendingDevicesUseCase.execute().map(deviceGeneralInfoMapper::map)
    }

    @GetMapping("devices/active")
    fun getActive(): List<GeneralDeviceInfo> {
        return getActiveDevicesUseCase.execute().map(deviceGeneralInfoMapper::map)
    }


    @GetMapping("/devices/{id}")
    fun getDetails(@PathVariable id: Long): DeviceDetails {
        val device = getDeviceByIdUseCase.runCatching { execute(id) }.getOrElse { throw notFound }
        return deviceDetailsMapper.map(device)
    }
}