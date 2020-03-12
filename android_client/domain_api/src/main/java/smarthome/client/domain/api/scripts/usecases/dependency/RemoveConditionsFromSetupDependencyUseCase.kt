package smarthome.client.domain.api.scripts.usecases.dependency

import smarthome.client.entity.script.dependency.condition.DependencyUnitId

interface RemoveConditionsFromSetupDependencyUseCase {
    fun execute(vararg ids: DependencyUnitId)
}