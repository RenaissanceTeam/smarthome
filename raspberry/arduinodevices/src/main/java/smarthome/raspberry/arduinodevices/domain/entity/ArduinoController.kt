package smarthome.raspberry.arduinodevices.domain.entity

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import smarthome.raspberry.entity.ID_NOT_DEFINED
import smarthome.raspberry.entity.controller.Controller
import javax.persistence.*

@Entity
data class ArduinoController(

        @Id @GeneratedValue
        val id: Long = ID_NOT_DEFINED,
        @OneToOne
        @OnDelete(action = OnDeleteAction.CASCADE)
        val controller: Controller,
        val serial: Int
)