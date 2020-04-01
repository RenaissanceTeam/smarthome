package smarthome.raspberry.scripts.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.domain.AddScriptUseCase
import smarthome.raspberry.scripts.data.ScriptsRepository

@Component
class AddScriptUseCaseImpl(
        private val repo: ScriptsRepository
) : AddScriptUseCase {

    override fun execute(script: Script): Script {
        return repo.save(script)
    }
}
