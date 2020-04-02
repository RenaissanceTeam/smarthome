package smarthome.raspberry.arduinodevices.script.domain.entity.dht

import smarthome.raspberry.entity.script.Condition
import javax.persistence.Entity

@Entity
data class HumidityCondition(
        override val id: String,
        val sign: String,
        val value: String
) : Condition(id)