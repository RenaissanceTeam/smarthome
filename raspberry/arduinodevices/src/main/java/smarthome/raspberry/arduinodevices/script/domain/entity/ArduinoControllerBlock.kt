package smarthome.raspberry.arduinodevices.script.domain.entity

import com.fasterxml.jackson.annotation.JsonIdentityReference
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.controller.Controller
import smarthome.raspberry.entity.script.Position
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
open class ArduinoControllerBlock(
        id: String,
        position: Position,
        @ManyToOne
        @JsonIdentityReference(alwaysAsId = true)
        val controller: Controller
) : Block(id, position)