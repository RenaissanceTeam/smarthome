package smarthome.client.entity.script.dependency.condition

import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.DependencyUnit

data class DependencyCondition(
    val dependencyId: DependencyId,
    val units: List<DependencyUnit>
)