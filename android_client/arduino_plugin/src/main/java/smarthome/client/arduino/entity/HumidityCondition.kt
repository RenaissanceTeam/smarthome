package smarthome.client.arduino.entity

import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.condition.controller.ControllerCondition

data class HumidityCondition(
    override val controllerId: Long,
    override val dependencyId: DependencyId,
    val humidity: String? = null,
    val sign: String? = null
): ControllerCondition

