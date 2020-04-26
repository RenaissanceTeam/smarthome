package smarthome.raspberry.arduinodevices.script.domain.entity.onoff

import smarthome.raspberry.entity.script.Condition
import javax.persistence.Entity

@Entity
data class OnOffCondition(
        override val id: String,
        val value: String
) : Condition(id)

