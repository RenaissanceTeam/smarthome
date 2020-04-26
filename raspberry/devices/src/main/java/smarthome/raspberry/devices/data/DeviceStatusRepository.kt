package smarthome.raspberry.devices.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.entity.device.Device
import smarthome.raspberry.entity.device.DeviceStatus

interface DeviceStatusRepository: JpaRepository<DeviceStatus, Long> {
    fun findByStatus(status: String): List<DeviceStatus>
    fun findByDevice(device: Device): DeviceStatus?
}