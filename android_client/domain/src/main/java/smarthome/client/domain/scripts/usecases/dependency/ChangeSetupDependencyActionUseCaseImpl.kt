package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyActionUseCase
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.util.findAndModify

class ChangeSetupDependencyActionUseCaseImpl(
    private val repo: SetupDependencyRepo
) : ChangeSetupDependencyActionUseCase {
    override fun execute(action: Action) {
        val details = repo.get()
        val changedActions = details.actions.findAndModify(
            predicate = { it.id == action.id },
            modify = { action }
        )
        repo.set(details.copy(actions = changedActions))
    }
}