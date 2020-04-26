package smarthome.client.domain.scripts.usecases.setup

import io.reactivex.Observable
import smarthome.client.data.api.scripts.SetupScriptRepo
import smarthome.client.domain.api.scripts.usecases.setup.ObserveSetupScriptUseCase
import smarthome.client.entity.script.Script

class ObserveSetupScriptUseCaseImpl(
    private val repo: SetupScriptRepo
) : ObserveSetupScriptUseCase {
    override fun execute(): Observable<Script> {
        return repo.observeScript()
    }
}