package smarthome.client.domain.api.scripts.usecases.dependency

import smarthome.client.entity.script.dependency.action.Action

interface ChangeSetupDependencyActionUseCase {
    fun execute(action: Action)
}