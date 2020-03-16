package smarthome.client.domain.api.scripts.usecases.dependency

import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.entity.script.dependency.condition.DependencyUnitId

interface ChangeSetupDependencyConditionUseCase {
    fun execute(id: DependencyUnitId, partialUpdate: (Condition) -> Condition)
}