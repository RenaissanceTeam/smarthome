package smarthome.raspberry.scripts.api.domain

import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Script

interface GetScriptByContainedBlockUseCase {
    fun execute(block: Block): Script
}