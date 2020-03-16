package smarthome.client.domain.api.scripts.usecases.dependency

import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.DependencyUnitId

interface ChangeSetupDependencyActionUseCase {
    fun execute(id: DependencyUnitId, partialUpdate: (Action) -> Action)
}