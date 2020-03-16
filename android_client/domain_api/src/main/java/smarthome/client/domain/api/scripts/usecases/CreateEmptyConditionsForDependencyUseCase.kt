package smarthome.client.domain.api.scripts.usecases

import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.condition.Condition

interface CreateEmptyConditionsForDependencyUseCase {
    fun execute(scriptId: Long, dependency: Dependency): List<Condition>
}