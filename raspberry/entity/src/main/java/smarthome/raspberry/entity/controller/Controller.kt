package smarthome.raspberry.entity.controller

import smarthome.raspberry.entity.ID_NOT_DEFINED
import smarthome.raspberry.entity.device.Device
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Controller(
        @Id @GeneratedValue
        val id: Long = ID_NOT_DEFINED,
        @ManyToOne
        val device: Device,
        val type: String,
        val name: String = "",
        val state: String?
)