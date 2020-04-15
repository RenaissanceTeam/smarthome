package smarthome.raspberry.arduinodevices.domain.entity

import smarthome.raspberry.entity.ID_NOT_DEFINED
import smarthome.raspberry.entity.controller.Controller
import javax.persistence.*

@Entity
data class ArduinoController(

        @Id @GeneratedValue
        val id: Long = ID_NOT_DEFINED,
        @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
        val controller: Controller,
        val serial: Int
)