package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition

interface UpdateDependencyDetailsUseCase {
    fun execute(scriptId: Long, dependencyId: DependencyId,
                conditions: List<Condition>, action: Action)
}