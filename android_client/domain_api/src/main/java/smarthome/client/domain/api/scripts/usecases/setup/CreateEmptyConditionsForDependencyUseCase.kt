package smarthome.client.domain.api.scripts.usecases.setup

import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.condition.Condition

interface CreateEmptyConditionsForDependencyUseCase {
    fun execute(dependency: Dependency): List<Condition>
}