package smarthome.raspberry.scripts.domain.usecase

import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.RegisteredProtocolsRepository
import smarthome.raspberry.scripts.api.domain.usecase.RemoveScriptUseCase
import smarthome.raspberry.scripts.data.ScriptsRepository

@Component
class RemoveScriptUseCaseImpl(
        private val scriptsRepository: ScriptsRepository,
        private val registeredProtocolsRepository: RegisteredProtocolsRepository
) : RemoveScriptUseCase {
    override fun execute(id: Long) {
        if (registeredProtocolsRepository.isRegistered(id)) registeredProtocolsRepository.unregister(id)

        scriptsRepository.deleteById(id)
    }
}