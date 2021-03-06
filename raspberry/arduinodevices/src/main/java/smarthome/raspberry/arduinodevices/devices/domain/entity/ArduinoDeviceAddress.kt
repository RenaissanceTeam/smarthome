package smarthome.raspberry.arduinodevices.devices.domain.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import smarthome.raspberry.entity.device.Device
import javax.persistence.*

@Entity
data class ArduinoDeviceAddress(
        @Id @GeneratedValue
        val id: Long = 0,
        @OneToOne
        @OnDelete(action = OnDeleteAction.CASCADE)
        val device: Device,
        val address: String,
        val port: String = "80"
)