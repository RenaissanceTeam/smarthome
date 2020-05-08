package smarthome.raspberry.scripts.domain.usecase

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Dependency
import smarthome.raspberry.scripts.api.domain.usecase.GetBlockByIdUseCase
import smarthome.raspberry.scripts.api.domain.usecase.GetBlockConditionDependenciesUseCase
import smarthome.raspberry.scripts.api.domain.usecase.GetScriptByContainedBlockUseCase

@Component
class GetBlockConditionDependenciesUseCaseImpl(
        private val getScriptByContainedBlockUseCase: GetScriptByContainedBlockUseCase,
        private val getBlockByIdUseCase: GetBlockByIdUseCase
) : GetBlockConditionDependenciesUseCase {
    override fun execute(id: String): List<Dependency> {
        val block = getBlockByIdUseCase.execute(id)

        return getScriptByContainedBlockUseCase.execute(block)
                .dependencies
                .filter { it.start == block }
    }
}