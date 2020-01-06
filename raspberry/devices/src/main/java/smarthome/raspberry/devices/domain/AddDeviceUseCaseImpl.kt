package smarthome.raspberry.devices.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.AddDeviceUseCase
import smarthome.raspberry.devices.api.domain.dto.DeviceDTO
import smarthome.raspberry.devices.data.DeviceStatusRepository
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.devices.domain.entity.DeviceStatus
import smarthome.raspberry.devices.domain.entity.DeviceStatuses
import smarthome.raspberry.devices.domain.mapper.DeviceDtoToDeviceMapper

@Component
class AddDeviceUseCaseImpl(
    private val devicesRepository: DevicesRepository,
    private val deviceDtoMapper: DeviceDtoToDeviceMapper,
    private val deviceStatusRepository: DeviceStatusRepository

) : AddDeviceUseCase {
    override fun execute(device: DeviceDTO) {
        if (devicesRepository.findBySerialName(device.serialName) != null) {
            TODO()
        }
        
        deviceDtoMapper.map(device).let {
            devicesRepository.save(it)
            deviceStatusRepository.save(DeviceStatus(
                device = it,
                status = DeviceStatuses.PENDING.name
            ))
        }
    }
}