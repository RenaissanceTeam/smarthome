package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.GetScriptByIdUseCase
import smarthome.client.domain.api.scripts.usecases.setup.StartSetupScriptUseCase
import smarthome.client.entity.script.Script
import smarthome.client.util.log

class StartSetupScriptUseCaseImpl(
    private val repo: SetupScriptRepo,
    private val getScriptByIdUseCase: GetScriptByIdUseCase
) : StartSetupScriptUseCase {
    override suspend fun execute(id: Long?): Script {
        val script = id?.let { getScriptByIdUseCase.execute(it) } ?: Script()
        repo.setScript(script)
        
        return script
    }
}