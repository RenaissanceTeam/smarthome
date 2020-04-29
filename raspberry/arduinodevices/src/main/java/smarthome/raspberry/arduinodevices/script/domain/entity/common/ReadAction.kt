package smarthome.raspberry.arduinodevices.script.domain.entity.common

import smarthome.raspberry.entity.script.Action
import javax.persistence.Entity

@Entity
data class ReadAction(
        override val id: String
) : Action(id)



