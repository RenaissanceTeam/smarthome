package smarthome.raspberry.devices.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.entity.Device

interface DevicesRepository: JpaRepository<Device, Long> {
    fun findBySerialName(serialName: String): Device?
//    fun saveDevice(device: Device)
//    fun savePendingDevice(device: Device)
//    fun addPendingDevice(device: Device)
//    fun removePendingDevice(device: Device)
//    fun addDevice(device: Device)
//    fun removeDevice(device: Device)
//    fun getCurrentDevices(): List<Device>
}