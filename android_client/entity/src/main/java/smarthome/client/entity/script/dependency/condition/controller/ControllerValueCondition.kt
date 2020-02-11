package smarthome.client.entity.script.dependency.condition.controller

import smarthome.client.entity.script.dependency.DependencyId

open class ControllerValueCondition(
    override val controllerId: Long,
    override val dependencyId: DependencyId,
    val value: String? = null,
    val sign: String? = null
) : ControllerCondition {
    
    fun withSign(sign: String) = ControllerValueCondition(
        controllerId, dependencyId, value, sign
    )
    
    fun withValue(value: String) = ControllerValueCondition(
        controllerId, dependencyId, value, sign
    )
}
