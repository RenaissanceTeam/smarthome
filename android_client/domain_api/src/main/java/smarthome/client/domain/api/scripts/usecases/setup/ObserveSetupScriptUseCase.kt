package smarthome.client.domain.api.scripts.usecases.setup

import io.reactivex.Observable
import smarthome.client.entity.script.Script

interface ObserveSetupScriptUseCase {
    fun execute(): Observable<Script>
}