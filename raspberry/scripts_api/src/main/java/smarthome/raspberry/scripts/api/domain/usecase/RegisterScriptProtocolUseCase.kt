package smarthome.raspberry.scripts.api.domain.usecase

import io.reactivex.disposables.Disposable
import smarthome.raspberry.entity.script.Script

interface RegisterScriptProtocolUseCase {
    fun execute(script: Script): Disposable

}