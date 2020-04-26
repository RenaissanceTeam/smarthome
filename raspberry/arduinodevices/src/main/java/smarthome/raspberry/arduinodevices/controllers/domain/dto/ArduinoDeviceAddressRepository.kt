package smarthome.raspberry.arduinodevices.controllers.domain.dto

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.arduinodevices.devices.domain.entity.ArduinoDeviceAddress
import smarthome.raspberry.entity.device.Device

interface ArduinoDeviceAddressRepository : JpaRepository<ArduinoDeviceAddress, Long> {
    fun findByDevice(device: Device): ArduinoDeviceAddress
}