package smarthome.client.arduino.entity.condition

import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.condition.controller.ControllerCondition

data class TemperatureCondition(
    override val controllerId: Long,
    override val dependencyId: DependencyId,
    val temperature: String? = null,
    val sign: String? = null
): ControllerCondition