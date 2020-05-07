package smarthome.raspberry.devices.data.controller

import org.springframework.web.bind.annotation.*
import smarthome.raspberry.devices.api.data.dto.DeviceDetails
import smarthome.raspberry.devices.api.data.dto.GeneralDeviceInfo
import smarthome.raspberry.devices.api.domain.*
import smarthome.raspberry.devices.data.mapper.DeviceToDeviceDetailsMapper
import smarthome.raspberry.devices.data.mapper.DeviceToGeneralDeviceInfoMapper
import smarthome.raspberry.entity.device.Device
import smarthome.raspberry.util.StringRequestBody
import smarthome.raspberry.util.exceptions.notFound

@RestController
@RequestMapping("api/")
class DevicesController(
        private val getDevicesUseCase: GetDevicesUseCase,
        private val getPendingDevicesUseCase: GetPendingDevicesUseCase,
        private val getActiveDevicesUseCase: GetActiveDevicesUseCase,
        private val acceptUseCase: AcceptPendingDeviceUseCase,
        private val declineUseCase: DeclinePendingDeviceUseCase,
        private val deviceGeneralInfoMapper: DeviceToGeneralDeviceInfoMapper,
        private val getDeviceByIdUseCase: GetDeviceByIdUseCase,
        private val deviceDetailsMapper: DeviceToDeviceDetailsMapper,
        private val updateDeviceUseCase: UpdateDeviceUseCase

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
        return deviceDetailsMapper.map(getDeviceById(id))
    }

    private fun getDeviceById(id: Long): Device {
        return getDeviceByIdUseCase.runCatching { execute(id) }.getOrElse { throw notFound }
    }

    @PostMapping("/devices/{id}/accept")
    fun accept(@PathVariable id: Long) {
        acceptUseCase.execute(getDeviceById(id))
    }

    @PostMapping("/devices/{id}/decline")
    fun decline(@PathVariable id: Long) {
        declineUseCase.execute(getDeviceById(id))
    }

    @PatchMapping("/devices/{id}/name")
    fun updateName(@PathVariable id: Long, @RequestBody name: StringRequestBody): DeviceDetails {
        return updateDeviceUseCase.execute(id) { it.copy(name = name.value) }
                .let(deviceDetailsMapper::map)
    }

    @PatchMapping("/devices/{id}/description")
    fun updateDescription(@PathVariable id: Long, @RequestBody description: StringRequestBody): DeviceDetails {
        return updateDeviceUseCase.execute(id) { it.copy(description = description.value) }
                .let(deviceDetailsMapper::map)
    }
}