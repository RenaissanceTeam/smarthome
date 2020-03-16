package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.action.Action

interface CreateEmptyActionForDependencyUseCase {
    fun execute(scriptId: Long, dependency: Dependency): List<Action>
}