package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.action.Action

interface CreateEmptyActionForDependencyUseCase {
    fun execute(dependency: Dependency): List<Action>
}