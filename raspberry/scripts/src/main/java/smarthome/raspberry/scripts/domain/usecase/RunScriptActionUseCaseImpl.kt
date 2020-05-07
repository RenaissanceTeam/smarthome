package smarthome.raspberry.scripts.domain.usecase

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Action
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.scripts.api.domain.usecase.RunScriptActionUseCase

@Component
class RunScriptActionUseCaseImpl : RunScriptActionUseCase {
    override fun execute(block: Block, action: Action) {

    }
}