package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.UpdateScriptInfoUseCase
import smarthome.client.entity.script.ScriptInfo

class UpdateScriptInfoUseCaseImpl(
    private val repo: SetupScriptRepo
) : UpdateScriptInfoUseCase {
    override fun execute(info: ScriptInfo) {
        repo.setScriptInfo(info)
    }
}