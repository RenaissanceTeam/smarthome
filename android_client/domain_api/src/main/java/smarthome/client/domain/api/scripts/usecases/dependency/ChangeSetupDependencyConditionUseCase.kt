package smarthome.client.domain.api.scripts.usecases.dependency

import smarthome.client.entity.script.dependency.condition.Condition

interface ChangeSetupDependencyConditionUseCase {
    fun execute(condition: Condition)
}