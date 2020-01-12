package smarthome.raspberry.arduinodevices.domain

import smarthome.raspberry.entity.Device
import javax.persistence.*

@Entity
data class ArduinoDeviceAddress(
        @Id @GeneratedValue
        val id: Long = 0,
        @OneToOne
        @JoinColumn
        val device: Device,
        val address: String
)