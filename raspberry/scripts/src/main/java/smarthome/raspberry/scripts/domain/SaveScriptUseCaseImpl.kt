package smarthome.raspberry.scripts.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.domain.RegisterScriptProtocolUseCase
import smarthome.raspberry.scripts.api.domain.SaveScriptUseCase
import smarthome.raspberry.scripts.data.ScriptsRepository

@Component
class SaveScriptUseCaseImpl(
        private val repo: ScriptsRepository,
        private val registerScriptProtocolUseCase: RegisterScriptProtocolUseCase
) : SaveScriptUseCase {

    override fun execute(script: Script): Script {
        return repo.save(script).also {
            registerScriptProtocolUseCase.execute(it)
        }
    }
}
