package smarthome.raspberry.arduinodevices.script.domain.entity.analog

import smarthome.raspberry.entity.script.Condition
import javax.persistence.Entity

@Entity
data class AnalogCondition(
        override val id: String,
        val value: String
) : Condition(id)