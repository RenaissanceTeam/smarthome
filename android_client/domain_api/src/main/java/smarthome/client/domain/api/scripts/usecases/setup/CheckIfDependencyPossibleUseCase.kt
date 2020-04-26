package smarthome.client.domain.api.scripts.usecases.setup

interface CheckIfDependencyPossibleUseCase {
    fun execute(from: String, to: String): Boolean
}