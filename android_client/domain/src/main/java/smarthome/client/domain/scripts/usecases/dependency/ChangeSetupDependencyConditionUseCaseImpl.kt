package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyConditionUseCase
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.entity.script.dependency.condition.DependencyUnitId
import smarthome.client.util.findAndModify

class ChangeSetupDependencyConditionUseCaseImpl(
    private val repo: SetupDependencyRepo
) : ChangeSetupDependencyConditionUseCase {
    override fun execute(id: DependencyUnitId, partialUpdate: (Condition) -> Condition) {
        val dependency = repo.get()
        val changedConditions = dependency.conditions.findAndModify(
            predicate = { it.id == id },
            modify = { partialUpdate(it) }
        )
        repo.set(dependency.copy(conditions = changedConditions))
    }
}