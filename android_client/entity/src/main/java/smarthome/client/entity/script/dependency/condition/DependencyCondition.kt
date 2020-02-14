package smarthome.client.entity.script.dependency.condition

import smarthome.client.entity.script.dependency.DependencyId

data class DependencyCondition(
    val dependencyId: DependencyId,
    val units: List<Condition>
)