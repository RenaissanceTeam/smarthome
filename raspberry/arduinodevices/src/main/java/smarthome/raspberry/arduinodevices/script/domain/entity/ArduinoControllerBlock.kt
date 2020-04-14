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
        open val type: String,
        @ManyToOne
        @JsonIdentityReference(alwaysAsId = true)
        open val controller: Controller
) : Block(id, position)