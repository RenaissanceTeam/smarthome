package smarthome.raspberry.scripts.domain.usecase

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.domain.usecase.RegisterScriptProtocolUseCase
import smarthome.raspberry.scripts.api.domain.usecase.SetScriptEnabledUseCase
import smarthome.raspberry.scripts.api.domain.usecase.UnregisterScriptUseCase
import smarthome.raspberry.scripts.data.ScriptsRepository
import smarthome.raspberry.util.exceptions.notFound

@Component
class SetScriptEnabledUseCaseImpl(
        private val scriptsRepository: ScriptsRepository,
        private val registerScriptProtocolUseCase: RegisterScriptProtocolUseCase,
        private val unregisterScriptUseCase: UnregisterScriptUseCase
) : SetScriptEnabledUseCase {
    override fun execute(id: Long, isEnabled: Boolean) {
        val script = scriptsRepository.findByIdOrNull(id) ?: throw notFound
        scriptsRepository.save(
                Script(
                        id = script.id,
                        name = script.name,
                        description = script.description,
                        enabled = isEnabled,
                        dependencies = script.dependencies,
                        blocks = script.blocks
                )
        )

        when (isEnabled) {
            true -> registerScriptProtocolUseCase.execute(script)
            false -> unregisterScriptUseCase.execute(script.id)
        }
    }
}