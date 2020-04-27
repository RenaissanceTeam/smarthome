package smarthome.raspberry.arduinodevices.script.domain.entity.digital

import smarthome.raspberry.entity.script.Condition
import javax.persistence.Entity

@Entity
data class DigitalCondition(
        override val id: String,
        val value: String
) : Condition(id)


