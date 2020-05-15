package smarthome.client.domain.api.scripts

interface RemoveScriptUseCase {
    suspend fun execute(id: Long)
}