package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.CancelSetupScriptUseCase

class CancelSetupScriptUseCaseImpl(
    private val repo: SetupScriptRepo
) : CancelSetupScriptUseCase {
    override fun execute() {
        repo.reset()
    }
}