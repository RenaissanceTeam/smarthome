package smarthome.raspberry.scripts.domain.usecase

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.data.RegisteredProtocolsRepository
import smarthome.raspberry.scripts.api.domain.usecase.SaveScriptUseCase
import smarthome.raspberry.scripts.data.ScriptsRepository

@Component
class SaveScriptUseCaseImpl(
        private val repo: ScriptsRepository,
        private val protocolsRepository: RegisteredProtocolsRepository
) : SaveScriptUseCase {

    override fun execute(script: Script): Script {
        return repo.save(script).also {
            protocolsRepository.register(it.id)
        }
    }
}
