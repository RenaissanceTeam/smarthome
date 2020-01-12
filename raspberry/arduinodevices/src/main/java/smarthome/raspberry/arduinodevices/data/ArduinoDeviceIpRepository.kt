package smarthome.raspberry.arduinodevices.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.arduinodevices.domain.ArduinoDeviceIp
import smarthome.raspberry.entity.Device

interface ArduinoDeviceIpRepository : JpaRepository<ArduinoDeviceIp, Long> {
    fun findByDevice(device: Device): ArduinoDeviceIp
}