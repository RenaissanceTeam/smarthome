package smarthome.raspberry.scripts.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Action
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.scripts.api.domain.RunScriptActionUseCase

@Component
class RunScriptActionUseCaseImpl : RunScriptActionUseCase {
    override fun execute(block: Block, action: Action) {

    }
}