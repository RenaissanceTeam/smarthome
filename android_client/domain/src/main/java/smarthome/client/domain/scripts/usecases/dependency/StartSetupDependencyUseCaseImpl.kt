package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.exceptions.NoActionsForBlock
import smarthome.client.domain.api.scripts.exceptions.NoConditionsForBlock
import smarthome.client.domain.api.scripts.usecases.CreateEmptyActionForBlockUseCase
import smarthome.client.domain.api.scripts.usecases.CreateEmptyConditionsForBlockUseCase
import smarthome.client.domain.api.scripts.usecases.GetDependencyDetailsUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.StartSetupDependencyUseCase
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId

class StartSetupDependencyUseCaseImpl(
    private val repo: SetupDependencyRepo,
    private val createEmptyConditionsForBlockUseCase: CreateEmptyConditionsForBlockUseCase,
    private val createEmptyActionForBlockUseCase: CreateEmptyActionForBlockUseCase,
    private val getDependencyDetailsUseCase: GetDependencyDetailsUseCase
) : StartSetupDependencyUseCase {
    override fun execute(scriptId: Long, dependencyId: DependencyId): DependencyDetails {
        repo.setScript(scriptId)
        return getDependencyDetailsUseCase.execute(scriptId, dependencyId)
            .let { details -> addConditionsIfEmpty(details, scriptId) }
            .let { details -> addActionsIfEmpty(details, scriptId) }
            .apply { repo.set(this) }
    }
    
    private fun addConditionsIfEmpty(details: DependencyDetails, scriptId: Long): DependencyDetails {
        return when (details.conditions.isEmpty()) {
            true -> withFirstEmptyCondition(scriptId, details)
            false -> details
        }
    }
    
    
    private fun addActionsIfEmpty(details: DependencyDetails, scriptId: Long): DependencyDetails {
        return when (details.actions.isEmpty()) {
            true -> withFirstEmptyAction(scriptId, details)
            false -> details
        }
    }
    
    
    private fun withFirstEmptyCondition(scriptId: Long, details: DependencyDetails): DependencyDetails {
        val allConditions = createEmptyConditionsForBlockUseCase
            .execute(scriptId, details.dependency.startBlock)
            .also {
                if (it.isEmpty()) {
                    throw NoConditionsForBlock(details.dependency.startBlock, "Can't start setup.")
                }
            }
        return details.copy(conditions = listOf(allConditions.first()))
    }
    
    
    private fun withFirstEmptyAction(scriptId: Long, details: DependencyDetails): DependencyDetails {
        val allActions = createEmptyActionForBlockUseCase
            .execute(scriptId, details.dependency.endBlock)
            .also {
                if (it.isEmpty()) {
                    throw NoActionsForBlock(details.dependency.endBlock, "Can't start setup.")
                }
            }
        return details.copy(actions = listOf(allActions.first()))
    }
}