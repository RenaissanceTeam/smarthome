package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.ScriptsRepo
import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.SaveSetupScriptUseCase

class SaveSetupScriptUseCaseImpl(
    private val repo: SetupScriptRepo,
    private val scriptsRepo: ScriptsRepo
) : SaveSetupScriptUseCase {
    override suspend fun execute() {
        val script = repo.getScript() ?: throw IllegalStateException("can't save script that is null")
    
        scriptsRepo.save(script)
        repo.reset()
    }
}