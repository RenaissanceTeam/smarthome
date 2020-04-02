package smarthome.client.domain.api.scripts.usecases.setup

interface RemoveDependencyUseCase {
    fun execute(dependencyId: String)
}