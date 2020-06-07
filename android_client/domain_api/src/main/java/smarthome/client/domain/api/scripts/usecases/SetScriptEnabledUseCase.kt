package smarthome.client.domain.api.scripts.usecases

interface SetScriptEnabledUseCase {
    suspend fun execute(id: Long, isEnabled: Boolean)
}