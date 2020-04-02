package smarthome.client.arduino.entity.condition

import smarthome.client.entity.script.dependency.condition.Condition

data class HumidityCondition(
    override val id: String?,
    val controllerId: Long,
    val value: String? = null,
    val sign: String? = null
) : Condition()

