package smarthome.client.domain.scripts.usecases.setup

import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.IsSetupInProgressUseCase

class IsSetupInProgressUseCaseImpl(
    private val repo: SetupScriptRepo
) : IsSetupInProgressUseCase {
    override fun execute(id: Long): Boolean {
        return repo.getScript()?.id == id
    }
}