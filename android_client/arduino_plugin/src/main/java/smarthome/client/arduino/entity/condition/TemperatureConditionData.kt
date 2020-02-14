package smarthome.client.arduino.entity.condition

import smarthome.client.entity.script.dependency.condition.controller.ControllerValueConditionData

data class TemperatureConditionData(
    override val controllerId: Long,
    override val value: String? = null,
    override val sign: String? = null
) : ControllerValueConditionData