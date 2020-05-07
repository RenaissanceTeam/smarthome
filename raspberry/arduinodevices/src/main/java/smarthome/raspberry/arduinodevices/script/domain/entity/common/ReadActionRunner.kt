package smarthome.raspberry.arduinodevices.script.domain.entity.common

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.script.domain.entity.ArduinoControllerBlock
import smarthome.raspberry.controllers.api.domain.ReadControllerUseCase
import smarthome.raspberry.entity.script.Action
import smarthome.raspberry.scripts.api.domain.ActionRunner
import smarthome.raspberry.scripts.api.domain.usecase.GetBlockByIdUseCase

@Component
class ReadActionRunner(
        private val readControllerUseCase: ReadControllerUseCase,
        private val getBlockByIdUseCase: GetBlockByIdUseCase
): ActionRunner {
    override fun runAction(action: Action, blockId: String) {
        val block = getBlockByIdUseCase.execute(blockId)

        require(action is ReadAction)
        require(block is ArduinoControllerBlock)

        readControllerUseCase.execute(block.controller)
    }
}