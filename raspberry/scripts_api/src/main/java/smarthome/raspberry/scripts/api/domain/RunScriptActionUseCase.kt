package smarthome.raspberry.scripts.api.domain

import smarthome.raspberry.entity.script.Action
import smarthome.raspberry.entity.script.Block

interface RunScriptActionUseCase {
    fun execute(block: Block, action: Action)
}