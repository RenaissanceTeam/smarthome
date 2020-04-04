package smarthome.raspberry.devices.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.entity.device.Device

interface DevicesRepository: JpaRepository<Device, Long> {
    fun findBySerialName(serialName: String): Device?
}