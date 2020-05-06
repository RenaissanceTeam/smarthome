package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.domain.api.scripts.usecases.setup.CheckIfCanStartDependencyFromUseCase
import smarthome.client.domain.api.scripts.usecases.setup.GetBlockUseCase

class CheckIfCanStartDependencyFromUseCaseImpl(
        private val getBlockUseCase: GetBlockUseCase,
        private val resolver: ConditionFromBlockResolver
) : CheckIfCanStartDependencyFromUseCase {
    override fun execute(blockId: String): Boolean {
        return resolver.runCatching { resolve(getBlockUseCase.execute(blockId)) }.getOrNull() != null
    }
}