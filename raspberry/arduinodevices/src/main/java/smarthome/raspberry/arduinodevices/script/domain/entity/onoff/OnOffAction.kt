package smarthome.raspberry.arduinodevices.script.domain.entity.onoff

import smarthome.raspberry.entity.script.Action
import javax.persistence.Entity

@Entity
data class OnOffAction(
        override val id: String,
        val value: String
) : Action(id)