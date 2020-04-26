package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.GetSetupScriptUseCase
import smarthome.client.entity.script.Script

class GetSetupScriptUseCaseImpl(
    private val repo: SetupScriptRepo
) : GetSetupScriptUseCase {
    override fun execute(): Script? {
        return repo.getScript()
    }
}