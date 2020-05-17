package smarthome.client.domain.scripts.usecases

import smarthome.client.data.api.scripts.ScriptsRepo
import smarthome.client.domain.api.scripts.usecases.SetScriptEnabledUseCase

class SetScriptEnabledUseCaseImpl(
        private val repo: ScriptsRepo
) : SetScriptEnabledUseCase {

    override suspend fun execute(id: Long, isEnabled: Boolean) {
        repo.setEnabled(id, isEnabled)
    }
}