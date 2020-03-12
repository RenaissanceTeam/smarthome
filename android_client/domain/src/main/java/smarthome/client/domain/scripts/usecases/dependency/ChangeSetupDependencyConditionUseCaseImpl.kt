package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyConditionUseCase
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.util.findAndModify
import smarthome.client.util.log

class ChangeSetupDependencyConditionUseCaseImpl(
    private val repo: SetupDependencyRepo
) : ChangeSetupDependencyConditionUseCase {
    
    override fun execute(condition: Condition) {
        val details = repo.get()
        val changedConditions = details.conditions.findAndModify(
            predicate = { it.id == condition.id },
            modify = { condition }
        )
        repo.set(details.copy(conditions = changedConditions))
    }
}