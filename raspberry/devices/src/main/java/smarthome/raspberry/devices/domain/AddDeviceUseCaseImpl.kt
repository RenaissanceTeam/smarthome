package smarthome.raspberry.devices.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.devices.api.domain.AddDeviceUseCase
import smarthome.raspberry.devices.api.domain.dto.DeviceDTO
import smarthome.raspberry.devices.api.domain.exceptions.DeviceAlreadyExists
import smarthome.raspberry.devices.data.DeviceStatusRepository
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.devices.domain.mapper.DeviceDtoToDeviceMapper
import smarthome.raspberry.entity.device.Device
import smarthome.raspberry.entity.device.DeviceStatus
import smarthome.raspberry.entity.device.DeviceStatuses

@Component
class AddDeviceUseCaseImpl(
        private val devicesRepository: DevicesRepository,
        private val deviceDtoMapper: DeviceDtoToDeviceMapper,
        private val deviceStatusRepository: DeviceStatusRepository
) : AddDeviceUseCase {
    override fun execute(deviceDto: DeviceDTO): Device {
        if (devicesRepository.findBySerial(deviceDto.serial) != null) {
            throw DeviceAlreadyExists()
        }

        val newDevice = deviceDtoMapper.map(deviceDto)
        return devicesRepository.save(newDevice).also {
            deviceStatusRepository.save(DeviceStatus(
                    device = it,
                    status = DeviceStatuses.PENDING.name
            ))
        }
    }
}