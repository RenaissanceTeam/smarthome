package smarthome.raspberry.scripts.domain.usecase

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.domain.usecase.GetScriptByContainedBlockUseCase
import smarthome.raspberry.scripts.data.ScriptsRepository
import smarthome.raspberry.util.exceptions.notFound

@Component
class GetScriptByContainedBlockUseCaseImpl(
        private val repo: ScriptsRepository
) : GetScriptByContainedBlockUseCase {
    override fun execute(block: Block): Script {
        return repo.findByBlocks(block) ?: throw notFound
    }
}