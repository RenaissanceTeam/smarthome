package smarthome.raspberry.scripts.api.domain.usecase

import smarthome.raspberry.entity.script.Action
import smarthome.raspberry.entity.script.Block

interface RunScriptActionUseCase {
    fun execute(block: Block, action: Action)
}