package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.exceptions.NoActionsForBlock
import smarthome.client.domain.api.scripts.exceptions.NoConditionsForBlock
import smarthome.client.domain.api.scripts.usecases.CreateEmptyActionForBlockUseCase
import smarthome.client.domain.api.scripts.usecases.CreateEmptyConditionsForBlockUseCase
import smarthome.client.domain.api.scripts.usecases.GetDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.StartSetupDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyId

class StartSetupDependencyUseCaseImpl(
    private val repo: SetupDependencyRepo,
    private val createEmptyConditionsForBlockUseCase: CreateEmptyConditionsForBlockUseCase,
    private val createEmptyActionForBlockUseCase: CreateEmptyActionForBlockUseCase,
    private val getDependencyUseCase: GetDependencyUseCase
) : StartSetupDependencyUseCase {
    override fun execute(scriptId: Long, dependencyId: DependencyId): Dependency {
        repo.setScript(scriptId)
        return getDependencyUseCase.execute(scriptId, dependencyId)
            .let { dependency -> addConditionsIfEmpty(dependency, scriptId) }
            .let { dependency -> addActionsIfEmpty(dependency, scriptId) }
            .apply { repo.set(this) }
    }
    
    private fun addConditionsIfEmpty(dependency: Dependency, scriptId: Long): Dependency {
        return when (dependency.conditions.isEmpty()) {
            true -> withFirstEmptyCondition(scriptId, dependency)
            false -> dependency
        }
    }
    
    private fun addActionsIfEmpty(dependency: Dependency, scriptId: Long): Dependency {
        return when (dependency.actions.isEmpty()) {
            true -> withFirstEmptyAction(scriptId, dependency)
            false -> dependency
        }
    }
    
    private fun withFirstEmptyCondition(scriptId: Long, dependency: Dependency): Dependency {
        val allConditions = createEmptyConditionsForBlockUseCase
            .execute(scriptId, dependency.startBlock)
            .also {
                if (it.isEmpty()) {
                    throw NoConditionsForBlock(dependency.startBlock, "Can't start setup.")
                }
            }
        return dependency.copy(conditions = listOf(allConditions.first()))
    }
    
    private fun withFirstEmptyAction(scriptId: Long, dependency: Dependency): Dependency {
        val allActions = createEmptyActionForBlockUseCase
            .execute(scriptId, dependency.endBlock)
            .also {
                if (it.isEmpty()) {
                    throw NoActionsForBlock(dependency.endBlock, "Can't start setup.")
                }
            }
        return dependency.copy(actions = listOf(allActions.first()))
    }
}