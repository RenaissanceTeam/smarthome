package smarthome.raspberry.entity.controller

import smarthome.raspberry.entity.device.Device
import smarthome.raspberry.entity.ID_NOT_DEFINED
import javax.persistence.*

@Entity
data class Controller(
        @Id @GeneratedValue
    val id: Long = ID_NOT_DEFINED,
        @ManyToOne
    val device: Device,
        val type: String,
        val name: String,
        val state: String
)