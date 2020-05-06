package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.domain.api.scripts.usecases.setup.CheckIfDependencyPossibleUseCase
import smarthome.client.domain.api.scripts.usecases.setup.GetBlockUseCase


class CheckIfDependencyPossibleUseCaseImpl(
        private val getBlockUseCase: GetBlockUseCase,
        private val actionFromBlockResolver: ActionFromBlockResolver
) : CheckIfDependencyPossibleUseCase {
    override fun execute(from: String, to: String): Boolean {
        return actionFromBlockResolver.runCatching { resolve(getBlockUseCase.execute(to)) }
                .getOrNull() != null
    }
}