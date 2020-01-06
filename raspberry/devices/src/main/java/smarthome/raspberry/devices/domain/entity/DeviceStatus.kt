package smarthome.raspberry.devices.domain.entity

import javax.persistence.Entity

@Entity
data class DeviceStatus(
    val device: Device,
    val status: String
)

