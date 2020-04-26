package smarthome.raspberry.arduinodevices.script.domain.entity.onoff

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.script.domain.entity.ArduinoControllerBlock
import smarthome.raspberry.controllers.api.domain.WriteControllerUseCase
import smarthome.raspberry.entity.script.Action
import smarthome.raspberry.scripts.api.domain.ActionRunner
import smarthome.raspberry.scripts.api.domain.GetBlockByIdUseCase

@Component
class OnOffActionRunner(
        private val writeControllerUseCase: WriteControllerUseCase,
        private val getBlockByIdUseCase: GetBlockByIdUseCase
) : ActionRunner {

    override fun runAction(action: Action, blockId: String) {
        val block = getBlockByIdUseCase.execute(blockId)

        require(action is OnOffAction)
        require(block is ArduinoControllerBlock)

        writeControllerUseCase.execute(block.controller, action.value)
    }
}