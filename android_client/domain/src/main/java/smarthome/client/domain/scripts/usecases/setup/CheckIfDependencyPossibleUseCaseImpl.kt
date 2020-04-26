package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.domain.api.scripts.usecases.setup.CheckIfDependencyPossibleUseCase


class CheckIfDependencyPossibleUseCaseImpl : CheckIfDependencyPossibleUseCase {
    override fun execute(from: String, to: String): Boolean {
        return true
    }
}