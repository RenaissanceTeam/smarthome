package smarthome.raspberry.scripts.domain.usecase

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.domain.usecase.GetAllScriptsUseCase
import smarthome.raspberry.scripts.data.ScriptsRepository

@Component
class GetAllScriptsUseCaseImpl(
        private val repo: ScriptsRepository
) : GetAllScriptsUseCase {
    override fun execute(): List<Script> {
        return repo.findAll()
    }
}