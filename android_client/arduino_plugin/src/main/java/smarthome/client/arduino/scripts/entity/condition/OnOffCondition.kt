package smarthome.client.arduino.scripts.entity.condition

import smarthome.client.entity.script.dependency.condition.Condition

data class OnOffCondition(
        override val id: String,
        val value: String? = null
) : Condition()

