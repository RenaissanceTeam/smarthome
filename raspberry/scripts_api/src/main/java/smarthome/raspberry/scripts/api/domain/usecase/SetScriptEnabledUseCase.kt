package smarthome.raspberry.scripts.api.domain.usecase

interface SetScriptEnabledUseCase {
    fun execute(id: Long, isEnabled: Boolean)
}