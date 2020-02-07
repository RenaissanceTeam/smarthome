package smarthome.client.entity.script.dependency.condition.controller

import smarthome.client.entity.script.dependency.DependencyId

data class ControllerValueCondition(
    override val controllerId: Long,
    override val dependencyId: DependencyId,
    val value: String,
    val sign: String
): ControllerCondition
