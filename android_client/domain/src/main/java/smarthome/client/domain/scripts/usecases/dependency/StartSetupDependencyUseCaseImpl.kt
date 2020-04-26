package smarthome.client.domain.scripts.usecases.dependency

import smarthome.client.data.api.scripts.SetupDependencyRepo
import smarthome.client.domain.api.scripts.exceptions.NoActionsForBlock
import smarthome.client.domain.api.scripts.exceptions.NoConditionsForBlock
import smarthome.client.domain.api.scripts.usecases.setup.CreateEmptyActionForDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.setup.CreateEmptyConditionsForDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.setup.GetDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.StartSetupDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
class StartSetupDependencyUseCaseImpl(
    private val repo: SetupDependencyRepo,
    private val createEmptyConditionsForDependencyUseCase: CreateEmptyConditionsForDependencyUseCase,
    private val createEmptyActionForDependencyUseCase: CreateEmptyActionForDependencyUseCase,
    private val getDependencyUseCase: GetDependencyUseCase
) : StartSetupDependencyUseCase {
    override fun execute(dependencyId: String): Dependency {
        
        return getDependencyUseCase.execute(dependencyId)
            .let { dependency -> addConditionsIfEmpty(dependency) }
            .let { dependency -> addActionsIfEmpty(dependency) }
            .apply { repo.set(this) }
    }
    
    private fun addConditionsIfEmpty(dependency: Dependency): Dependency {
        return when (dependency.conditions.isEmpty()) {
            true -> withFirstEmptyCondition(dependency)
            false -> dependency
        }
    }
    
    private fun addActionsIfEmpty(dependency: Dependency): Dependency {
        return when (dependency.actions.isEmpty()) {
            true -> withFirstEmptyAction(dependency)
            false -> dependency
        }
    }
    
    private fun withFirstEmptyCondition(dependency: Dependency): Dependency {
        val allConditions = createEmptyConditionsForDependencyUseCase
            .execute(dependency)
            .also {
                if (it.isEmpty()) {
                    throw NoConditionsForBlock(dependency.startBlock, "Can't start setup.")
                }
            }
        return dependency.copy(conditions = listOf(allConditions.first()))
    }
    
    private fun withFirstEmptyAction(dependency: Dependency): Dependency {
        val allActions = createEmptyActionForDependencyUseCase
            .execute(dependency)
            .also {
                if (it.isEmpty()) {
                    throw NoActionsForBlock(dependency.endBlock, "Can't start setup.")
                }
            }
        return dependency.copy(actions = listOf(allActions.first()))
    }
}