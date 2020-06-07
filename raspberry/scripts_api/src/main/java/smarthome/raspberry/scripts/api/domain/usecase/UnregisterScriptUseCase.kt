package smarthome.raspberry.scripts.api.domain.usecase

interface UnregisterScriptUseCase {
    fun execute(id: Long)
}