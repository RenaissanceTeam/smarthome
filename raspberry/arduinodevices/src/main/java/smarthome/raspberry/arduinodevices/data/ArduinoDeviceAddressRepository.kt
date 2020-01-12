package smarthome.raspberry.arduinodevices.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.arduinodevices.domain.ArduinoDeviceAddress
import smarthome.raspberry.entity.Device

interface ArduinoDeviceAddressRepository : JpaRepository<ArduinoDeviceAddress, Long> {
    fun findByDevice(device: Device): ArduinoDeviceAddress
}