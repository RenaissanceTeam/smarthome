package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.SaveSetupScriptUseCase

class SaveSetupScriptUseCaseImpl(
    private val repo: SetupScriptRepo
) : SaveSetupScriptUseCase {
    override suspend fun execute() {
        val script = repo.getScript()
        TODO("save $script to server")
    }
}