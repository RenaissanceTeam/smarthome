package smarthome.raspberry.arduinodevices.domain.entity

import smarthome.raspberry.entity.ID_NOT_DEFINED
import smarthome.raspberry.entity.controller.Controller
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class ArduinoController(

        @Id @GeneratedValue
        val id: Long = ID_NOT_DEFINED,
        val controller: Controller,
        val serial: Int
)