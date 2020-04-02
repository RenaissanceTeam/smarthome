package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyActionUseCase
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.util.findAndModify

class ChangeSetupDependencyActionUseCaseImpl(
    private val repo: SetupDependencyRepo
) : ChangeSetupDependencyActionUseCase {
    
    override fun execute(id: String, partialUpdate: (Action) -> Action) {
        val dependency = repo.get()
        val changedActions = dependency.actions.findAndModify(
            predicate = { it.id == id },
            modify = { partialUpdate(it) }
        )
        repo.set(dependency.copy(actions = changedActions))
    }
}