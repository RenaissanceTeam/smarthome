package smarthome.raspberry.scripts.domain.usecase

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.domain.usecase.GetScriptByIdUseCase
import smarthome.raspberry.scripts.data.ScriptsRepository
import smarthome.raspberry.util.exceptions.notFound

@Component
class GetScriptByIdUseCaseImpl(
        private val repository: ScriptsRepository
) : GetScriptByIdUseCase {
    override fun execute(id: Long): Script {
        return repository.findById(id).runCatching { get() }.getOrElse { throw notFound }
    }
}