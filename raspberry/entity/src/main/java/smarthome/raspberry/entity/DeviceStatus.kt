package smarthome.raspberry.entity

import javax.persistence.*

@Entity
data class DeviceStatus(
    @Id @GeneratedValue
    val id: Long = 0,
    @JoinColumn(name = "deviceId")
    @OneToOne
    val device: Device,
    val status: String
)
