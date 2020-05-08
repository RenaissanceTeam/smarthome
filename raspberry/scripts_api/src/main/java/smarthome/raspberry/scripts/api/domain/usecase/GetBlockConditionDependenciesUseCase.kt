package smarthome.raspberry.scripts.api.domain.usecase

import smarthome.raspberry.entity.script.Dependency

interface GetBlockConditionDependenciesUseCase {
    fun execute(id: String): List<Dependency>
}