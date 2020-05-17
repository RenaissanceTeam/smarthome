package smarthome.raspberry.scripts.domain.usecase

import org.springframework.stereotype.Component
import smarthome.raspberry.scripts.api.data.RegisteredProtocolsRepository
import smarthome.raspberry.scripts.api.domain.usecase.UnregisterScriptUseCase

@Component
class UnregisterScriptUseCaseImpl(
        private val registeredProtocolsRepository: RegisteredProtocolsRepository
) : UnregisterScriptUseCase {
    override fun execute(id: Long) {
        if (registeredProtocolsRepository.isRegistered(id)) registeredProtocolsRepository.unregister(id)
    }
}