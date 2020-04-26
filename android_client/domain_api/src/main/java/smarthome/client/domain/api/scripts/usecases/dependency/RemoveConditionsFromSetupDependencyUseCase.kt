package smarthome.client.domain.api.scripts.usecases.dependency

interface RemoveConditionsFromSetupDependencyUseCase {
    fun execute(vararg ids: String)
}