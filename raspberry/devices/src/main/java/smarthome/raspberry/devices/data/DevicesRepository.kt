package smarthome.raspberry.devices.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.domain.entity.Device

interface DevicesRepository: JpaRepository<Device, Long> {
    fun findBySerialName(serialName: String): Device?
//    fun saveDevice(device: IotDevice)
//    fun savePendingDevice(device: IotDevice)
//    fun addPendingDevice(device: IotDevice)
//    fun removePendingDevice(device: IotDevice)
//    fun addDevice(device: IotDevice)
//    fun removeDevice(device: IotDevice)
//    fun getCurrentDevices(): List<IotDevice>
}