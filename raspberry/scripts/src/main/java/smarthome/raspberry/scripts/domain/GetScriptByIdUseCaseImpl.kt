package smarthome.raspberry.scripts.domain

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.domain.GetScriptByIdUseCase
import smarthome.raspberry.scripts.data.ScriptsRepository
import smarthome.raspberry.util.exceptions.notFound

@Component
class GetScriptByIdUseCaseImpl(
        private val repository: ScriptsRepository
) : GetScriptByIdUseCase {
    override fun execute(id: Long): Script {
        return repository.findByIdOrNull(id) ?: throw notFound
    }
}